import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Customer {
    private final static String QUEUE_NAME = "rabbitMQ.test";
    public static void main(String[] args) throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("ayt");
        factory.setPassword("...");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        System.out.println("Customer Waiting Received messages");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                      String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
