package com.example.ecomarket.api;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.OrderService;
import com.example.ecomarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductApi {
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("find/product/by/{categoryId}")
    public List<ProductResponse> findAllProductByCategoryId(
            @PathVariable Long categoryId) {
        return productService.searchProductByTitleAndCategoryId(categoryId);
    }

    @GetMapping("search/product/by/title")
    public List<ProductResponse> findAllProductByCategoryId(
            @RequestParam String title) {
        return productService.searchProductByTitle(title);
    }

    @GetMapping("search/product/by/title/by/{categoryId}")
    public List<ProductResponse> findAllProductByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam String title) {
        return productService.searchProductByTitleAndCategoryId(title, categoryId);
    }

//    @PostMapping
//    @PreAuthorize("isAuthenticated()")
//    public String hello(@RequestParam Long id) {
//        return orderService.save(id, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
}