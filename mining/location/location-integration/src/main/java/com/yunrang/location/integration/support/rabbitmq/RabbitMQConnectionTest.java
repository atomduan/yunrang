package com.yunrang.location.integration.support.rabbitmq;


import java.util.HashMap;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.Connection;

public class RabbitMQConnectionTest {
	private static Address[] addrArr = new Address[]{new Address("127.0.0.1", 5672)};
	private static Connection connection;  
	private static Channel channel;  
	private static String requestQueueName = "storm_sub_queue";
	private static String exchangeName = "test_exchange";
	
	@SuppressWarnings({ "unchecked", "serial" })
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		connection = factory.newConnection(addrArr);
		channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName,"fanout");  
		String queueName = channel.queueDeclare(requestQueueName, false, false, true, new HashMap(){{put("x-max-length", 1000);}}).getQueue();
        channel.queueBind(queueName, exchangeName, "");
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        channel.basicConsume(queueName, true, consumer);
        while(true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");  
        }
	}
}
