package com.product_service.feign;

import com.product_service.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "AUTHSERVICEAPP")
public interface AuthServiceFeignClient {

    @GetMapping("/api/v1/auth/get-user")
    User getUser(@RequestParam("username") String username, @RequestHeader("Authorization") String token);
}