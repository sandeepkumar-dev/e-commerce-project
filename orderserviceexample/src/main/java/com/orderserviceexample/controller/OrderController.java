package com.orderserviceexample.controller;

import com.orderserviceexample.dto.OrderEvent;
import com.orderserviceexample.kafka.OrderProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer producer;

    public OrderController(OrderProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String placeOrder(@RequestBody OrderEvent event) {

        producer.sendOrderEvent(event);

        return "Order Event Published Successfully";
    }
}