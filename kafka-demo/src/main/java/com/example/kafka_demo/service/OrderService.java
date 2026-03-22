package com.example.kafka_demo.service;

import com.example.kafka_demo.model.Order;
import com.example.kafka_demo.producer.OrderProducer;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderProducer producer;

    public OrderService(OrderProducer producer) {
        this.producer = producer;
    }

    public void createOrder(Order order) {
        producer.sendOrder(order);
    }
}
