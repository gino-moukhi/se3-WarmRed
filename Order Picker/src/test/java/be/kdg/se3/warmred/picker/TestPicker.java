package be.kdg.se3.warmred.picker;

import be.kdg.se3.warmred.picker.adapters.*;
import be.kdg.se3.warmred.picker.domain.CommunicationException;
import be.kdg.se3.warmred.picker.domain.MessageHandler;
import be.kdg.se3.warmred.picker.domain.MessageInputService;
import be.kdg.se3.warmred.util.Sleeper;
import org.junit.Before;
import org.junit.Test;

public class TestPicker {
    public static void main(String[] args) {
        Sleeper sleeper = new Sleeper();
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String messagequeue = "Internal: Orders";
        MessageFormatter formatter = new XmlMessageFormatter();

        MessageInputService inputService = new RabbitMQ(uri,messagequeue,formatter);
        ApiService api = new LocationServiceApi();
        Converter converter = new LocationConverter();

        MessageHandler handler = new MessageHandler(inputService);
        handler.setApiService(api);
        handler.setConverter(converter);
        handler.start();

        try {
            inputService.declareConsumer(handler);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

    private MessageInputService inputService;
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
    }
}
