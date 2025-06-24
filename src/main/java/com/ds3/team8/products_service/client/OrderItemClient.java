package com.ds3.team8.products_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ds3.team8.products_service.config.FeignClientInterceptor;


@FeignClient(name = "orders-service", configuration = FeignClientInterceptor.class)
public interface OrderItemClient {
    @GetMapping("/api/v1/order-items/product/{productId}/exists")
    Boolean orderItemHasProducts(@PathVariable("productId") Long productId);
}