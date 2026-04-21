package com.order_service.service;

import com.order_service.client.CartFeignClient;
import com.order_service.dto.CartDto;
import com.order_service.dto.CartItemDto;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final CartFeignClient cartFeignClient;
    private final OrderRepository orderRepository;

    public OrderService(CartFeignClient cartFeignClient, OrderRepository orderRepository) {
        this.cartFeignClient = cartFeignClient;
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(String uuid) {

        // 1. Fetch Cart from Cart Service
        CartDto cart = cartFeignClient.getCart(uuid);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Create Order
        Order order = new Order();
        order.setCartUuid(uuid);
        order.setUserId(cart.getUserId());
        order.setOrderStatus("CREATED");

        BigDecimal total = BigDecimal.ZERO;

        // 3. Convert CartItems → OrderItems
        for (CartItemDto cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setBrandId(cartItem.getBrandId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order);

            order.getItems().add(orderItem);

            total = total.add(
                    cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalAmount(total);

        // 4. Save Order
        Order savedOrder = orderRepository.save(order);

        // 5. Clear Cart via API
        cartFeignClient.clearCart(uuid);

        return savedOrder;
    }

    public boolean markOrderStatus(long orderId) {

        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus("Completed");
        Order savedOrder = orderRepository.save(order);

        if(order.getOrderStatus().equals("Completed")){

            return  true;
        }
        return false;
    }


}
