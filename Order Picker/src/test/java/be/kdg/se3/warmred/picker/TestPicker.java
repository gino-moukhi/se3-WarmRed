package be.kdg.se3.warmred.picker;

import be.kdg.se3.warmred.picker.adapters.*;
import be.kdg.se3.warmred.picker.domain.*;
import be.kdg.se3.warmred.util.Sleeper;
import org.junit.Before;
import org.junit.Test;

public class TestPicker {
    public static void main(String[] args) {
        Sleeper sleeper = new Sleeper();
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String readQueue = "Internal: Orders";
        String writeQueue = "Internal: Logistics";
        MessageFormatter formatter = new XmlMessageFormatter();

        MessageInputService inputService = new RabbitMQ(uri,readQueue,formatter);
        MessageOutputService outputService = new RabbitMQ(uri,writeQueue,formatter);
        ApiService api = new LocationServiceApi();
        Converter converter = new LocationConverter();
        PickingService pickingService = new PickingImpl();
        PickingType pickingType = PickingType.SINGLE;
        //PickingType pickingType = PickingType.GROUP;

        MessageHandler handler = new MessageHandler(inputService, outputService);
        handler.setApiService(api);
        handler.setConverter(converter);
        handler.setPickingService(pickingService);
        handler.setPickingType(pickingType);
        handler.start();

        try {
            inputService.declareConsumer(handler);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

    /*private MessageInputService inputService;
    private ApiService api;
    private Converter converter;

    @Before
    public void setUp() {
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String messagequeue = "Internal: Orders";
        MessageFormatter formatter = new XmlMessageFormatter();


        inputService = new RabbitMQ(uri,messagequeue,formatter);
        api = new LocationServiceApi();
        converter = new LocationConverter();
    }

    @Test
    public void testRead() {
        MessageHandler handler = new MessageHandler(inputService);
        handler.setApiService(api);
        handler.setConverter(converter);
        handler.start();

        try {
            inputService.declareConsumer(handler);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }*/
}
