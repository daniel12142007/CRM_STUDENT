package com.example.ecomarket.api.user;

import com.example.ecomarket.dto.request.OrderRequest;
import com.example.ecomarket.dto.response.HistoryResponse;
import com.example.ecomarket.dto.response.OrderHisResponse;
import com.example.ecomarket.dto.response.OrderResponse;
import com.example.ecomarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderApi {
    private final OrderService orderService;


    @PostMapping("order")
    public OrderResponse order(@RequestBody OrderRequest request) {
        return orderService.order(
                request,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("history/{orderId}")
    public OrderHisResponse historyById(@PathVariable Long orderId) {
        return orderService.historyFindById(orderId);
    }

    @GetMapping("find/all/history")
    public List<HistoryResponse> findAllHistory() {
        return orderService.findAllHistory(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}