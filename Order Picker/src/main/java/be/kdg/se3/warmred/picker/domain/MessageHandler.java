package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.adapters.ApiService;
import be.kdg.se3.warmred.picker.adapters.Converter;
import be.kdg.se3.warmred.picker.adapters.ConverterException;
import be.kdg.se3.warmred.picker.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.CreateOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.Dto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

public class MessageHandler implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private MessageInputService messageInputService;
    private ApiService apiService;
    private Converter converter;
    private Set<LocationInfo> productMemoryCache;

    public MessageHandler(MessageInputService messageInputService) {
        this.messageInputService = messageInputService;
        productMemoryCache = new TreeSet<>();
    }

    public void start() {
        try {
            messageInputService.initialize();
        } catch (CommunicationException e) {
            logger.error("Unable to properly start the adapter");
        }
    }

    public void stop() {
        try {
            messageInputService.shutdown();
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
                LocationInfo locationInfo = checkCache(key);
                logger.info("Adding location info to the list. INFO: " + locationInfo.toString());
                createOrderMessage.addLocationInfo(locationInfo);
                logger.info("Location info in the list of CreateOrderMessage: " + createOrderMessage.getLocationInfoList().toString());
            });
            //TODO add picking order

            //TODO send new object to second queue

        } else if (dtoMessage instanceof CancelOrderMessageDto) {
            CancelOrderMessage cancelOrderMessage = new CancelOrderMessage((CancelOrderMessageDto) dtoMessage);
            logger.info("Ready to do stuff with CancelOrderMessage with id: " + cancelOrderMessage.getOrderId());
            //TODO implement with group buffer, failed order list or other
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

    private LocationInfo checkCache(int productId) {
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
                logger.error("Could not reach the api service");
            } catch (ConverterException e) {
                logger.info("Error in the conversion");
            }
        }
        return infoToReturn.get();
    }
}
