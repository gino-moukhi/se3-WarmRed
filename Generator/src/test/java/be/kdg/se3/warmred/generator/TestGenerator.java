package be.kdg.se3.warmred.generator;

import be.kdg.se3.warmred.generator.adapters.MessageFormatter;
import be.kdg.se3.warmred.generator.adapters.RabbitMQ;
import be.kdg.se3.warmred.generator.adapters.XmlMessageFormatter;
import be.kdg.se3.warmred.generator.domain.*;
import be.kdg.se3.warmred.generator.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.generator.domain.dto.CreateOrderMessageDto;
import be.kdg.se3.warmred.util.Sleeper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestGenerator {
    private final Logger logger = LoggerFactory.getLogger(RabbitMQ.class);
    private Sleeper sleeper;
    private MessageOutputService outputService;
    private Random random;
    private final int MIN_ID = 1000000;
    private final int MAX_ID = 2000000;
    private final int MAX_CUSTOMERLIST_ITEMS = 20;
    private final int MAX_PRODUCTLIST_ITEMS = 20;
    private List<Integer> customerList;
    private List<Integer> productList;
    private List<CreateOrderMessage> orderList;
    private List<CancelOrderMessage> cancelOrderList;

    @Before
    public void setUp() {
        sleeper = new Sleeper();
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String messagequeue = "Internal: Orders";
        MessageFormatter formatter = new XmlMessageFormatter();
        outputService = new RabbitMQ(uri, messagequeue, formatter);
        random = new Random();
        customerList = new ArrayList<>();
        productList = new ArrayList<>();
        orderList = new ArrayList<>();
        cancelOrderList = new CopyOnWriteArrayList<>();
    }

    @Test
    public void testBasicSend() {
        try {
            outputService.sendBasicMessage("Hello world");
        } catch (CommunicationException e) {
            logger.error("Could not send basic string message to message broker");
        }
    }

    @Test
    public void testCreateOrderMessageSend() {
        try {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(MIN_ID, 2);
            items.put(MIN_ID + 1, 3);
            items.put(MIN_ID + 2, 4);
            outputService.sendMessage(new CreateOrderMessage(MIN_ID, MIN_ID + 101, 100, items));
        } catch (CommunicationException e) {
            logger.error("Could not send basic create order message to message broker");
        }
    }

    @Test
    public void testCreateOrderMessageDtoSend() {
        try {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(MIN_ID, 2);
            items.put(MIN_ID + 1, 3);
            items.put(MIN_ID + 2, 4);
            outputService.sendMessage(new CreateOrderMessageDto(MIN_ID, MIN_ID + 101, 69, items));
        } catch (CommunicationException e) {
            logger.error("Could not send basic create order message dto to message broker");
        }
    }

    @Test
    public void testCancelOrderMessageSend() {
        try {
            outputService.sendMessage(new CancelOrderMessage(MIN_ID));
        } catch (CommunicationException e) {
            logger.error("Could not send basic cancel order message to message broker");
        }
    }

    @Test
    public void testCancelOrderMessageDtoSend() {
        try {
            outputService.sendMessage(new CancelOrderMessageDto(MIN_ID));
        } catch (CommunicationException e) {
            logger.error("Could not send basic cancel order message dto to message broker");
        }
    }

    @Test
    public void testCreateOrderMessageSendLoop() {
        try {
            fillCustomerList();
            fillProductList();

            for (int i = MIN_ID; i < MIN_ID + 5; i++) {
                sleeper.sleep(1000);
                outputService.sendMessage(generateRandomOrder(i));
                logger.info(generateRandomOrder(i).toString());
            }
        } catch (CommunicationException e) {
            logger.error("Could not send multiple create order messages to message broker");
        }
    }

    @Test
    public void testCreateOrderMessageDtoSendLoop() {
        try {
            fillCustomerList();
            fillProductList();

            for (int i = MIN_ID; i < MIN_ID + 5; i++) {
                sleeper.sleep(1000);
                outputService.sendMessage(generateRandomOrderDto(i));
                logger.info(generateRandomOrderDto(i).toString());
            }
        } catch (CommunicationException e) {
            logger.error("Could not send multiple create order message dto's to message broker");
        }
    }

    @Test
    public void TestFullGenerator() {
        int amountToTriggerCancel = 5;
        int secondsToTriggerCancel = 3;
        final int DELAY = 1000;
        int index = MIN_ID;

        fillCustomerList();
        fillProductList();

        for (int i = 0; i < 20; i++) {
            sleeper.sleep(DELAY);
            if (i % amountToTriggerCancel == 0 && !orderList.isEmpty()) {
                CancelOrderMessage cancelOrderMessage = generateRandomCancelOrder(orderList.get(getRandomIntBetweenInclusive(0, orderList.size() - 1)).getOrderId());
                logger.info(cancelOrderMessage.toString());
            }
            for (CancelOrderMessage c : cancelOrderList) {
                int simpleId = c.getOrderId() - 1000000 + 1;
                int timeToTrigger = (simpleId * DELAY) + (secondsToTriggerCancel * 1000); //e.g. timeToTrigger = 7500 ms
                if (timeToTrigger == sleeper.getAmountSleptInMiliseconds() || timeToTrigger < sleeper.getAmountSleptInMiliseconds()) {
                    logger.info("TIME TO SEND MESSAGE");
                    logger.info("ID: " + c.getOrderId());
                    logger.info("CURRENT INDEX: " + index);
                    try {
                        outputService.sendMessage(new CancelOrderMessageDto(c));
                    } catch (CommunicationException e) {
                        logger.error("Could not send cancel order message with ID: " + c.getOrderId());
                    }
                    cancelOrderList.removeIf(item -> item.getOrderId() == c.getOrderId());
                }

            }
            cancelOrderList.forEach(item -> logger.info("ITEMS TO CANCEL: " + item.toString()));
            /*
                Send cancel message every x seconds
            if (sleeper.getAmountSleptInMiliseconds() % (secondsToTriggerCancel * 1000) == 0 && !orderList.isEmpty()) {
                System.out.println(generateRandomCancelOrder(orderList.get(getRandomIntBetweenInclusive(0, orderList.size() - 1)).getOrderId()));
            }*/
            CreateOrderMessage orderMessage = generateRandomOrder(index++);
            logger.info(orderMessage.toString());
            try {
                outputService.sendMessage(new CreateOrderMessageDto(orderMessage));
            } catch (CommunicationException e) {
                logger.error("Could not send create order message with ID: " + orderMessage.getOrderId());
            }
        }
        //orderList.forEach(order -> System.out.println(order.getOrderId()));
    }

    @Test
    public void testSendErrorId() {
        try {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(1111111, 1);
            //items.put(2222222, 2);
            //items.put(999999, 3);
            //items.put(10000000, 4);
            CreateOrderMessageDto createOrderMessageDto = new CreateOrderMessageDto(1000000,1000000,100,items);
            System.out.println(createOrderMessageDto);
            outputService.sendMessage(createOrderMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getRandomIntBetweenInclusive(int min, int max) {
        return this.random.nextInt(max - min + 1) + min;
    }

    private void fillCustomerList() {
        for (int i = 0; i < MAX_CUSTOMERLIST_ITEMS; i++) {
            customerList.add(getRandomIntBetweenInclusive(MIN_ID, MAX_ID));
        }
    }

    private void fillProductList() {
        for (int i = 0; i < MAX_PRODUCTLIST_ITEMS; i++) {
            productList.add(getRandomIntBetweenInclusive(MIN_ID, MAX_ID));
        }
    }

    private CreateOrderMessage generateRandomOrder(int index) {
        CreateOrderMessage order = new CreateOrderMessage();
        order.setOrderId(index);
        order.setCustomerId(customerList.get(getRandomIntBetweenInclusive(0, customerList.size() - 1)));
        order.setPrice(getRandomIntBetweenInclusive(10, 100));
        order.setItems(new HashMap<>());
        for (int j = 0; j < getRandomIntBetweenInclusive(1, 4); j++) {
            order.addItem(productList.get(getRandomIntBetweenInclusive(0, productList.size() - 1)), getRandomIntBetweenInclusive(1, 5));
        }
        orderList.add(order);
        return order;
    }

    private CreateOrderMessageDto generateRandomOrderDto(int index) {
        CreateOrderMessageDto order = new CreateOrderMessageDto();
        order.setOrderId(index);
        order.setCustomerId(customerList.get(getRandomIntBetweenInclusive(0, customerList.size() - 1)));
        order.setPrice(getRandomIntBetweenInclusive(10, 100));
        order.setItems(new HashMap<>());
        for (int j = 0; j < getRandomIntBetweenInclusive(1, 4); j++) {
            order.addItem(productList.get(getRandomIntBetweenInclusive(0, productList.size() - 1)), getRandomIntBetweenInclusive(1, 5));
        }
        return order;
    }

    private CancelOrderMessage generateRandomCancelOrder(int id) {
        CancelOrderMessage order = new CancelOrderMessage(id);

        orderList.removeIf(item -> item.getOrderId() == id);

        cancelOrderList.add(order);
        return order;
    }

}
