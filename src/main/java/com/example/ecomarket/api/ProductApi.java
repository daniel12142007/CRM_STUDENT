package com.example.ecomarket.api;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductApi {
    private final ProductService productService;

    @GetMapping("find/product/by/{categoryId}")
    public List<ProductResponse> findAllProductByCategoryId(@PathVariable Long categoryId) {
        return productService.findAllProductByCategoryId(categoryId);
    }
}