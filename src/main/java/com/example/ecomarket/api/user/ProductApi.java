package com.example.ecomarket.api.user;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.OrderService;
import com.example.ecomarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductApi {
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping("add/product")
    public ProductResponse addProduct(@RequestParam Long id) {
        return orderService.saveProductOnOrder(
                id,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("get/sum/basket")
    public Integer sum() {
        return productService.basketSum(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    @GetMapping("find/product/by/{categoryId}")
    public List<ProductResponse> findAllProductByCategoryId(
            @PathVariable Long categoryId) {
        return productService.searchProductByTitleAndCategoryId(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                categoryId);
    }

    @GetMapping("find/by/{productId}")
    public ProductResponse findById(@PathVariable Long productId) {
        return productService.findById(
                productId,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("search/product/by/title")
    public List<ProductResponse> findAllProductByCategoryId(
            @RequestParam String title) {
        return productService.searchProductByTitle(
                title,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("search/product/by/title/by/{categoryId}")
    public List<ProductResponse> findAllProductByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam String title) {
        return productService.searchProductByTitleAndByTitle(
                title,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                categoryId);
    }

}