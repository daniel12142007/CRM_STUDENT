package com.example.ecomarket.api.user;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order/item")
public class OrderItemApi {
    private final OrderService orderService;

    @PutMapping("plus/one/product/{productId}")
    public ProductResponse plusOne(@PathVariable Long productId) {
        return orderService.plusOne(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                productId);
    }

    @PutMapping("minus/one/product/{productId}")
    public ProductResponse minusOne(@PathVariable Long productId) {
        return orderService.minusOne(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                productId);
    }
}