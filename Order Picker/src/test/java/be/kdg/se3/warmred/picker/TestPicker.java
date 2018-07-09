package be.kdg.se3.warmred.picker;

import be.kdg.se3.warmred.picker.adapters.MessageFormatter;
import be.kdg.se3.warmred.picker.adapters.RabbitMQ;
import be.kdg.se3.warmred.picker.adapters.XmlMessageFormatter;
import be.kdg.se3.warmred.picker.domain.CommunicationException;
import be.kdg.se3.warmred.picker.domain.MessageHandler;
import be.kdg.se3.warmred.picker.domain.MessageInputService;
import be.kdg.se3.warmred.util.Sleeper;

public class TestPicker {
    public static void main(String[] args) {
        Sleeper sleeper = new Sleeper();
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String messagequeue = "Internal: Orders";
        MessageFormatter formatter = new XmlMessageFormatter();

        MessageInputService inputService = new RabbitMQ(uri,messagequeue,formatter);

        MessageHandler handler = new MessageHandler(inputService);
        handler.start();

        try {
            inputService.declareConsumer(handler);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }
}
