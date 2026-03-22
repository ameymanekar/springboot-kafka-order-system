package com.example.kafka_demo.consumer;


import com.example.kafka_demo.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "${topic.name}", groupId = "order-group")
    public void consume(Order order) {
        System.out.println("Order received: " + order.getOrderId());
    }
}
