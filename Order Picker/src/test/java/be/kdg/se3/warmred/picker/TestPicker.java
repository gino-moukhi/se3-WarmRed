package be.kdg.se3.warmred.picker;

import be.kdg.se3.warmred.picker.adapters.*;
import be.kdg.se3.warmred.picker.domain.*;
import be.kdg.se3.warmred.picker.exceptions.CommunicationException;

class TestPicker {
    public static void main(String[] args) {
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String readQueue = "Internal: Orders";
        String writeQueue = "Internal: Logistics";
        MessageFormatter formatter = new XmlMessageFormatter();

        MessageInputService inputService = new RabbitMQ(uri,readQueue,formatter);
        MessageOutputService outputService = new RabbitMQ(uri,writeQueue,formatter);
        TaskScheduler scheduler = new TaskScheduler();
        ApiService api = new LocationServiceApi();
        Converter converter = new LocationConverter();
        PickingService pickingService = new PickingImpl();
        PickingType pickingType = PickingType.SINGLE;
        //PickingType pickingType = PickingType.GROUP;

        MessageHandler handler = new MessageHandler(inputService, outputService);
        handler.setTaskScheduler(scheduler);
        handler.setTaskIntervalClearCache(60);
        handler.setTaskIntervalRetryUnprocessed(120);
        handler.setApiService(api);
        handler.setConverter(converter);
        handler.setPickingService(pickingService);
        handler.setPickingType(pickingType);
        handler.setMaxBufferSize(5);

        handler.start();

        try {
            inputService.declareConsumer(handler);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }
}
