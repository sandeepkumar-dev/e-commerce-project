package com.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-service", url ="http://localhost:8083")
public interface OrderClient {

    @PutMapping("/api/v1/order/{orderId}")
    public boolean updateOrderStatus(
            @PathVariable ("orderId") long orderId);

}


