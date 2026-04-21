package com.order_service.client;

import com.order_service.dto.CartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "cart-service", url ="http://localhost:8082")
public interface CartFeignClient {

    @GetMapping("/api/v1/cart/{uuid}")
    CartDto getCart(@PathVariable String uuid);

    @DeleteMapping("/api/v1/cart/{uuid}/clear")
    void clearCart(@PathVariable String uuid);
}
