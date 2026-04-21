package com.order_service.controller;

import com.order_service.entity.Order;
import com.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestHeader("X-CART-ID") String uuid
    ) {
        Order order = orderService.placeOrder(uuid);
        return ResponseEntity.ok(Map.of(

                "message", "Order Placed Successfully",
                "orderId", order.getId(),
                "totalAmount", order.getTotalAmount()
        ));
    }
    @PutMapping("/{orderId}")
    public boolean updateOrderStatus(
            @PathVariable long orderId){
        boolean status = orderService.markOrderStatus(orderId);
       return  status;

    }
}
