package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.CommunicationException;
import be.kdg.se3.warmred.generator.domain.Message;
import be.kdg.se3.warmred.generator.domain.MessageOutputService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ implements MessageOutputService {
    private final Logger logger = LoggerFactory.getLogger(RabbitMQ.class);
    private final String connectionString;
    private final String queueName;
    private final MessageFormatter messageFormatter;
    private Connection connection;
    private Channel channel;


    public RabbitMQ(String connectionString, String queueName, MessageFormatter messageFormatter) {
        this.connectionString = connectionString;
        this.queueName = queueName;
        this.messageFormatter = messageFormatter;
    }

    @Override
    public void initialize() throws CommunicationException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(connectionString);
            factory.setRequestedHeartbeat(30);
            factory.setConnectionTimeout(30000);

            connection = factory.newConnection();
            channel = connection.createChannel();

            /*
                durable - RabbitMQ will never lose the queue if a crash occurs
                exclusive - if queue only will be used by one connection
                autoDelete - queue is deleted when last consumer unsubscribes
             */
            channel.queueDeclare(queueName, false, false, false, null);

        } catch (NoSuchAlgorithmException e) {
            logger.error("RabbitMQ connection initialization failed because of a 'NoSuchAlgorithmException' ");
        } catch (KeyManagementException e) {
            logger.error("RabbitMQ connection initialization failed because of a 'KeyManagementException' ");
        } catch (URISyntaxException e) {
            logger.error("RabbitMQ connection initialization failed because of a 'URISyntaxException' ");
        } catch (TimeoutException e) {
            logger.error("RabbitMQ connection initialization failed because of a 'TimeoutException' ");
        } catch (IOException e) {
            logger.error("RabbitMQ connection initialization failed because of an 'IOException' ");
        } catch (Exception e) {
            throw new CommunicationException("Something went wrong and it wasn't caught in any exception");
        }
    }

    @Override
    public void shutdown() throws CommunicationException {
        try {
            channel.close();
            connection.close();
        } catch (TimeoutException e) {
            logger.error("RabbitMQ shutdown failed because of a 'TimeoutException' ");
            throw new CommunicationException("Could not close connection to RabbitMQ", e);
        } catch (IOException e) {
            logger.error("RabbitMQ shutdown failed because of an 'IOException' ");
            throw new CommunicationException("Could not close connection to RabbitMQ", e);
        }
    }

    @Override
    public void sendMessage(Message message) throws CommunicationException {
        String formattedMessage;
        try {
            initialize();
            formattedMessage = messageFormatter.format(message);
            logger.info("Pushing message: " + formattedMessage);
            channel.basicPublish("", queueName, null, formattedMessage.getBytes());
        } catch (IOException e) {
            logger.error("Failed to send message to the queue");
            throw new CommunicationException("Could not send message to queue", e);
        } catch (FormatterException e) {
            logger.error("Could not send the message because an error occurred while formatting the message");
        }
    }

    public void sendBasicMessage(String body) {
        try {
            initialize();
            channel.basicPublish("", queueName, null, body.getBytes());
        } catch (CommunicationException | IOException e) {
            e.printStackTrace();
        }
    }
}
