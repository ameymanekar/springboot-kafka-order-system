# 🚀 Spring Boot Kafka Project (End-to-End Setup Guide)

## 📌 Project Overview

This project demonstrates a **complete event-driven architecture** using:

* Spring Boot (REST API)
* Apache Kafka (Producer & Consumer)
* Docker (Kafka setup)
* JSON Serialization

👉 Flow:
Client → REST API → Kafka Producer → Kafka Topic → Kafka Consumer

---

# 🧱 Tech Stack

* Java 17+
* Spring Boot 4.x
* Apache Kafka
* Docker & Docker Compose
* Maven

---

# 📁 Project Structure

```
com.example.kafka_demo
 ├── KafkaDemoApplication.java
 ├── controller
 │    └── OrderController.java
 ├── service
 │    └── OrderService.java
 ├── producer
 │    └── OrderProducer.java
 ├── consumer
 │    └── OrderConsumer.java
 ├── model
 │    └── Order.java
 └── resources
      └── application.yml
```

---

# ⚙️ Step 1: Create Project (Spring Initializr)

Go to: https://start.spring.io/

### Configuration:

* Project: Maven
* Language: Java
* Spring Boot: 4.x
* Dependencies:

  * Spring Web
  * Spring for Apache Kafka
  * Lombok (optional)

---

# 🐳 Step 2: Setup Kafka using Docker

## Create `docker-compose.yml` (in root folder)

```yaml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

## Run Kafka

```bash
docker-compose up -d
```

## Verify

```bash
docker ps
```

---

# ⚙️ Step 3: application.yml

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092

    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

    consumer:
      group-id: order-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"

topic:
  name: order-topic
```

---

# 🧩 Step 4: Model

```java
package com.example.kafka_demo.model;

public class Order {
    private String orderId;
    private String product;
    private int quantity;

    // getters & setters
}
```

---

# 🚀 Step 5: Producer

```java
package com.example.kafka_demo.producer;

import com.example.kafka_demo.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Value("${topic.name}")
    private String topic;

    public OrderProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Order order) {
        kafkaTemplate.send(topic, order);
        System.out.println("Order sent: " + order.getOrderId());
    }
}
```

---

# 📥 Step 6: Consumer

```java
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
```

---

# 🧠 Step 7: Service

```java
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
        producer.send(order);
    }
}
```

---

# 🌐 Step 8: Controller

```java
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
```

---

# ▶️ Step 9: Run Application

Using IntelliJ OR:

```bash
.\mvnw spring-boot:run
```

---

# 🧪 Step 10: Test API (Postman)

### Endpoint:

```
POST http://localhost:8080/orders
```

### Body:

```json
{
  "orderId": "1",
  "product": "Laptop",
  "quantity": 2
}
```

---

# 🎯 Expected Output

### API Response:

```
Order sent to Kafka!
```

### Console:

```
Order sent: 1
Order received: 1
```

---

# 🧠 Key Learnings

### 1. Kafka Serialization

* Object → JSON using JsonSerializer
* JSON → Object using JsonDeserializer

### 2. Common Errors & Fixes

| Error                | Fix                                    |
| -------------------- | -------------------------------------- |
| 404 Not Found        | Wrong package / controller not scanned |
| 400 Bad Request      | Missing JSON body                      |
| 500 Internal Error   | Serializer mismatch                    |
| ClassNotFound        | Wrong Jackson dependency               |
| Kafka not connecting | Wrong bootstrap server                 |

---

# ⚠️ Important Dependency Fix

❌ DO NOT use:

```xml
tools.jackson.core
```

✅ Use default:

```xml
spring-boot-starter-web
```

---

# 🐳 Useful Docker Commands

```bash
docker ps
docker logs kafka
docker-compose up -d
docker-compose down
docker rm -f kafka zookeeper
```

---

# 🚀 Future Enhancements

* Retry mechanism
* Dead Letter Queue (DLQ)
* Kafka UI dashboard
* Database integration
* Microservices split

---

# 🎉 Conclusion

You built a **real-world event-driven system**:

* Async communication
* Scalable architecture
* Production-ready foundation

---

# 👨‍💻 Author

Amey Manekar
Java Full Stack Developer 🚀

---
