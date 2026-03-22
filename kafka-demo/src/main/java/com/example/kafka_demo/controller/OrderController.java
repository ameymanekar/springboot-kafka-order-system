package com.example.kafka_demo.controller;

import com.example.kafka_demo.model.Order;
import com.example.kafka_demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        service.createOrder(order);
        return "Order sent to Kafka!";
    }
}
