import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work1 {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("ayt");
        factory.setPassword("...");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Worker1 waiting for messages");

        channel.basicQos(1);
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
               AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                System.out.println("Worker1 Received '"+ message + "'");
                doWork(message);
                System.out.println("Worker1 Done");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String task) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
