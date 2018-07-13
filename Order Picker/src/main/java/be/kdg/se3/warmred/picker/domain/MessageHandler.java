package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.adapters.ApiService;
import be.kdg.se3.warmred.picker.adapters.Converter;
import be.kdg.se3.warmred.picker.domain.dto.*;
import be.kdg.se3.warmred.picker.exceptions.CommunicationException;
import be.kdg.se3.warmred.picker.exceptions.ConverterException;
import be.kdg.se3.warmred.picker.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.MessageDto;
import be.kdg.se3.warmred.picker.exceptions.PickingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A service that is responsible for delegating tasks through the system
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class MessageHandler implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private TaskScheduler scheduler;
    private final MessageInputService messageInputService;
    private final MessageOutputService messageOutputService;
    private ApiService apiService;
    private Converter converter;
    private PickingService pickingService;
    private PickingType pickingType;
    private Set<LocationInfo> productMemoryCache;
    private List<CreateOrderMessage> pickingBuffer;
    private List<CreateOrderMessage> unprocessedOrders;
    private Runnable taskClearCache;
    private Runnable taskRetryUnprocessed;
    private int taskIntervalClearCache;
    private int taskIntervalRetryUnprocessed;
    private int maxBufferSize;

    public MessageHandler(MessageInputService messageInputService, MessageOutputService messageOutputService) {
        this.messageInputService = messageInputService;
        this.messageOutputService = messageOutputService;
        productMemoryCache = new TreeSet<>();
        pickingBuffer = new ArrayList<>();
        unprocessedOrders = new ArrayList<>();
        taskClearCache = productMemoryCache::clear;
        taskRetryUnprocessed = retryUnprocessedOrders();
    }

    public void start() {
        try {
            messageInputService.initialize();
            messageOutputService.initialize();
            scheduler.scheduleAtFixedRate(taskClearCache, taskIntervalClearCache);
            scheduler.scheduleAtFixedRate(taskRetryUnprocessed, taskIntervalRetryUnprocessed);
        } catch (CommunicationException e) {
            logger.error("Unable to properly start the adapter");
        }
    }

    public void stop() {
        try {
            messageInputService.shutdown();
            messageOutputService.shutdown();
        } catch (CommunicationException e) {
            logger.error("Unable to properly shut down the adapter");
        }
    }

    @Override
    public void onReceive(MessageDto messageDtoMessage) {
        if (messageDtoMessage instanceof CreateOrderMessageDto) {
            CreateOrderMessage createOrderMessage = new CreateOrderMessage((CreateOrderMessageDto) messageDtoMessage);
            createOrderMessage.getItems().keySet().forEach(key -> logger.info("Calling location service for productId: " + key));
            // Add location info
            assignLocationInfo(createOrderMessage);
            //Picking order
            assignPickingOrder(createOrderMessage);

        } else if (messageDtoMessage instanceof CancelOrderMessageDto) {
            CancelOrderMessage cancelOrderMessage = new CancelOrderMessage((CancelOrderMessageDto) messageDtoMessage);
            logger.info("Ready to do stuff with CancelOrderMessage with id: " + cancelOrderMessage.getOrderId());
            pickingBuffer.removeIf(order -> order.getOrderId() == cancelOrderMessage.getOrderId());
            unprocessedOrders.removeIf(order -> order.getOrderId() == cancelOrderMessage.getOrderId());
        } else {
            logger.warn("This statement should not be reached otherwise something went wrong while formatting");
        }
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public PickingService getPickingService() {
        return pickingService;
    }

    public void setPickingService(PickingService pickingService) {
        this.pickingService = pickingService;
    }

    public PickingType getPickingType() {
        return pickingType;
    }

    public void setPickingType(PickingType pickingType) {
        this.pickingType = pickingType;
    }

    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    public void setTaskScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public int getTaskIntervalClearCache() {
        return taskIntervalClearCache;
    }

    public void setTaskIntervalClearCache(int taskIntervalClearCache) {
        this.taskIntervalClearCache = taskIntervalClearCache;
    }

    public int getTaskIntervalRetryUnprocessed() {
        return taskIntervalRetryUnprocessed;
    }

    public void setTaskIntervalRetryUnprocessed(int taskIntervalRetryUnprocessed) {
        this.taskIntervalRetryUnprocessed = taskIntervalRetryUnprocessed;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    private LocationInfo checkCacheOrCallService(int productId) {
        AtomicReference<LocationInfo> infoToReturn = new AtomicReference<>();
        productMemoryCache.forEach(info -> {
            if (info.getProductID() == productId) {
                logger.info("Found product in memory cache with ID: " + productId);
                infoToReturn.set(info);
            }
        });
        if (infoToReturn.get() == null) {
            try {
                logger.info("Product not found in memory cache, calling api service");
                String jsonResponse = apiService.getJsonResponse(productId);
                LocationInfo locationInfo = converter.convertToLocationInfo(jsonResponse);
                infoToReturn.set(locationInfo);
                productMemoryCache.add(locationInfo);
            } catch (CommunicationException e) {
                logger.error("Could not reach the api service. ", e.getMessage());
                return new LocationInfo();
            } catch (ConverterException e) {
                logger.info("Error in the conversion. ", e.getMessage());
                return new LocationInfo();
            }
        }
        return infoToReturn.get();
    }

    private void assignLocationInfo(CreateOrderMessage createOrderMessage) {
        for (int key : createOrderMessage.getItems().keySet()) {
            LocationInfo locationInfo = checkCacheOrCallService(key);
            if (locationInfo.getProductID() == 0) {
                unprocessedOrders.add(createOrderMessage);
                logger.warn("Added order with ID: " + createOrderMessage.getOrderId() + " to the list of unprocessed orders");
            } else {
                logger.info("Adding location info to the list. INFO: " + locationInfo.toString());
                createOrderMessage.addLocationInfo(locationInfo);
                logger.info("Location info in the list of CreateOrderMessage: " + createOrderMessage.getLocationInfoList().toString());
            }
        }
    }

    private void assignPickingOrder(CreateOrderMessage createOrderMessage) {
        try {
            if (!unprocessedOrders.contains(createOrderMessage)) {
                if (pickingType.equals(PickingType.SINGLE)) {
                    logger.info("COMPLETE ORDER BEFORE SINGLE OPTIMIZATION: " + createOrderMessage.toString());
                    pickingService.optimize(createOrderMessage);
                    logger.info("COMPLETE ORDER AFTER SINGLE OPTIMIZATION: " + createOrderMessage.toString());
                    //SEND SINGLE DATA
                    messageOutputService.sendMessage(new ExtraCreateOrderMessageDto(createOrderMessage));
                } else if (pickingType.equals(PickingType.GROUP)) {
                    pickingBuffer.add(createOrderMessage);
                    if (pickingBuffer.size() == maxBufferSize) {
                        logger.info("COMPLETE BUFFER BEFORE GROUP OPTIMIZATION: " + pickingBuffer.toString());
                        pickingService.optimize(pickingBuffer);
                        logger.info("COMPLETE BUFFER AFTER GROUP OPTIMIZATION: " + pickingBuffer.toString());
                        //SENDING GROUP DATA
                        for (CreateOrderMessage m : pickingBuffer) {
                            messageOutputService.sendMessage(new ExtraCreateOrderMessageDto(m));
                        }
                        pickingBuffer.clear();
                    }
                } else {
                    throw new PickingException("Picking type not found (Should be single or group)");
                }
            } else {
                logger.warn("Order is inside the unprocessed order list");
            }
            logger.info("ITEMS IN THE PRODUCT MAP TOO SEE IF CLEAR CACHE ACTUALLY WORKS: " + productMemoryCache.toString());
        } catch (PickingException e) {
            logger.error("Something went wrong int the picking optimization");
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

    private Runnable retryUnprocessedOrders() {
        return () -> unprocessedOrders.forEach(item -> {
            assignLocationInfo(item);
            assignPickingOrder(item);
        });
    }
}
