package be.kdg.se3.warmred.generator;

import be.kdg.se3.warmred.generator.adapters.MessageFormatter;
import be.kdg.se3.warmred.generator.adapters.RabbitMQ;
import be.kdg.se3.warmred.generator.adapters.XmlMessageFormatter;
import be.kdg.se3.warmred.generator.domain.CommunicationException;
import be.kdg.se3.warmred.generator.domain.CreateOrderMessage;
import be.kdg.se3.warmred.generator.domain.MessageOutputService;
import org.junit.Before;
import org.junit.Test;

public class TestGenerator {
    private String uri;
    private String messagequeue;
    private MessageFormatter formatter;
    private MessageOutputService outputService;

    @Before
    public void setUp() {
        uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        messagequeue = "Internal: Orders";
        formatter = new XmlMessageFormatter();
        outputService = new RabbitMQ(uri, messagequeue, formatter);
    }

    @Test
    public void testBasicSend() {
        try {
            outputService.sendBasicMessage("Hello world");
            //outputService.sendMessage(new CreateOrderMessage());
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }
}
