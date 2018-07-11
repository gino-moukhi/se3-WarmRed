package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.adapters.ApiService;
import be.kdg.se3.warmred.picker.adapters.Converter;
import be.kdg.se3.warmred.picker.adapters.ConverterException;
import be.kdg.se3.warmred.picker.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.CreateOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.Dto;
import be.kdg.se3.warmred.picker.domain.dto.ExtraCreateOrderMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

public class MessageHandler implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final int MAX_BUFFER_SIZE = 5;
    private MessageInputService messageInputService;
    private MessageOutputService messageOutputService;
    private ApiService apiService;
    private Converter converter;
    private PickingService pickingService;
    private PickingType pickingType;
    private Set<LocationInfo> productMemoryCache;
    private List<CreateOrderMessage> pickingBuffer;
    private List<CreateOrderMessage> unprocessedOrders;

    public MessageHandler(MessageInputService messageInputService, MessageOutputService messageOutputService) {
        this.messageInputService = messageInputService;
        this.messageOutputService = messageOutputService;
        productMemoryCache = new TreeSet<>();
        pickingBuffer = new ArrayList<>();
        unprocessedOrders = new ArrayList<>();
    }

    public void start() {
        try {
            messageInputService.initialize();
            messageOutputService.initialize();
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
    public void onReceive(Dto dtoMessage) {
        if (dtoMessage instanceof CreateOrderMessageDto) {
            CreateOrderMessage createOrderMessage = new CreateOrderMessage((CreateOrderMessageDto) dtoMessage);
            createOrderMessage.getItems().keySet().forEach(key -> logger.info("Calling location service for productId: " + key));
            // Add location info
            createOrderMessage.getItems().keySet().forEach(key -> {
                LocationInfo locationInfo = checkCacheOrCallService(key);
                if (locationInfo.getProductID() == 0) {
                    unprocessedOrders.add(createOrderMessage);
                    logger.warn("Added order with ID: " + createOrderMessage.getOrderId() + " to the list of unprocessed orders");
                } else {
                    logger.info("Adding location info to the list. INFO: " + locationInfo.toString());
                    createOrderMessage.addLocationInfo(locationInfo);
                    logger.info("Location info in the list of CreateOrderMessage: " + createOrderMessage.getLocationInfoList().toString());
                }
            });
            //Picking order
            try {
                if (!unprocessedOrders.contains(createOrderMessage)) {
                    if (pickingType.equals(PickingType.SINGLE)) {
                        logger.info("COMPLETE ORDER BEFORE SINGLE OPTIMIZATION: " + createOrderMessage.toString());
                        pickingService.optimize(createOrderMessage);
                        logger.info("COMPLETE ORDER AFTER SINGLE OPTIMIZATION: " + createOrderMessage.toString());
                        //SEND NEW DATA
                        messageOutputService.sendMessage(new ExtraCreateOrderMessageDto(createOrderMessage));
                    } else if (pickingType.equals(PickingType.GROUP)) {
                        pickingBuffer.add(createOrderMessage);
                        if (pickingBuffer.size() == MAX_BUFFER_SIZE) {
                            logger.info("COMPLETE BUFFER BEFORE GROUP OPTIMIZATION: " + pickingBuffer.toString());
                            pickingService.optimize(pickingBuffer);
                            logger.info("COMPLETE BUFFER AFTER GROUP OPTIMIZATION: " + pickingBuffer.toString());
                            for (CreateOrderMessage m : pickingBuffer) {
                                //SENDING GROUP DATA
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
            } catch (PickingException e) {
                logger.error("Something went wrong int the picking optimization");
            } catch (CommunicationException e) {
                e.printStackTrace();
            }


        } else if (dtoMessage instanceof CancelOrderMessageDto) {
            CancelOrderMessage cancelOrderMessage = new CancelOrderMessage((CancelOrderMessageDto) dtoMessage);
            logger.info("Ready to do stuff with CancelOrderMessage with id: " + cancelOrderMessage.getOrderId());
            //TODO implement with group buffer, failed order list or other
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
}
