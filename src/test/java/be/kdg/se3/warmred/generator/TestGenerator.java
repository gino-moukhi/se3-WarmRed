package be.kdg.se3.warmred.generator;

import be.kdg.se3.warmred.generator.adapters.MessageFormatter;
import be.kdg.se3.warmred.generator.adapters.RabbitMQ;
import be.kdg.se3.warmred.generator.adapters.XmlMessageFormatter;
import be.kdg.se3.warmred.generator.domain.*;
import be.kdg.se3.warmred.generator.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.generator.domain.dto.CreateOrderMessageDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TestGenerator {
    private final Logger logger = LoggerFactory.getLogger(RabbitMQ.class);
    private MessageOutputService outputService;
    private Random random;
    private final int MIN_ID = 1000000;
    private final int MAX_ID = 2000000;
    private List<Integer> customerList;
    private List<Integer> productList;
    private List<CreateOrderMessage> orderList;

    @Before
    public void setUp() {
        String uri = "amqp://ktzwfisr:JTVB7eVafhTV53o5qDRjKzfDW1vbDvX6@sheep.rmq.cloudamqp.com/ktzwfisr";
        String messagequeue = "Internal: Orders";
        MessageFormatter formatter = new XmlMessageFormatter();
        outputService = new RabbitMQ(uri, messagequeue, formatter);
        random = new Random();
        customerList = new ArrayList<>();
        productList = new ArrayList<>();
        orderList = new ArrayList<>();
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

    @Test
    public void testCreateOrderMessageSend() {
        try {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(MIN_ID, 2);
            items.put(MIN_ID + 1, 3);
            items.put(MIN_ID + 2, 4);
            outputService.sendMessage(new CreateOrderMessage(MIN_ID, MIN_ID + 101, 69, items));
        } catch (CommunicationException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelOrderMessageSend() {
        try {
            outputService.sendMessage(new CancelOrderMessage(MIN_ID));
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelOrderMessageDtoSend() {
        try {
            outputService.sendMessage(new CancelOrderMessageDto(MIN_ID));
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateOrderMessageSendLoop() {
        try {
                fillCustomerList(20);
                fillProductList(20);

            for (int i = MIN_ID; i < MIN_ID + 5; i++) {
                Thread.sleep(1000);
                outputService.sendMessage(generateRandomOrder(i));
                //System.out.println(generateRandomOrder(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateOrderMessageDtoSendLoop() {
        try {
            fillCustomerList(20);
            fillProductList(20);

            for (int i = MIN_ID; i < MIN_ID + 5; i++) {
                Thread.sleep(1000);
                outputService.sendMessage(generateRandomOrderDto(i));
                //System.out.println(generateRandomOrderDto(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelOrderMessageSendLoop() {

    }

    @Test
    public void testCancelOrderMessageDtoSendLoop() {

    }

    private int getRandomIntBetweenInclusive(int min, int max) {
        return this.random.nextInt(max - min + 1) + min;
    }

    private void fillCustomerList(int amount) {
        for (int i = 0; i < amount; i++) {
            customerList.add(getRandomIntBetweenInclusive(MIN_ID, MAX_ID));
        }
    }

    private void fillProductList(int amount) {
        for (int i = 0; i < amount; i++) {
            productList.add(getRandomIntBetweenInclusive(MIN_ID, MAX_ID));
        }
    }

    private CreateOrderMessage generateRandomOrder(int index) {
        CreateOrderMessage order = new CreateOrderMessage();
        order.setOrderId(index);
        order.setCustomerId(customerList.get(getRandomIntBetweenInclusive(0, 9)));
        order.setPrice(getRandomIntBetweenInclusive(10, 69));
        order.setItems(new HashMap<>());
        for (int j = 0; j < getRandomIntBetweenInclusive(1, 4); j++) {
            order.addItem(productList.get(getRandomIntBetweenInclusive(0, 19)), getRandomIntBetweenInclusive(1, 5));
        }
        return order;
    }

    private CreateOrderMessageDto generateRandomOrderDto(int index) {
        CreateOrderMessageDto order = new CreateOrderMessageDto();
        order.setOrderId(index);
        order.setCustomerId(customerList.get(getRandomIntBetweenInclusive(0, 9)));
        order.setPrice(getRandomIntBetweenInclusive(10, 69));
        order.setItems(new HashMap<>());
        for (int j = 0; j < getRandomIntBetweenInclusive(1, 4); j++) {
            order.addItem(productList.get(getRandomIntBetweenInclusive(0, 19)), getRandomIntBetweenInclusive(1, 5));
        }
        return order;
    }
}
