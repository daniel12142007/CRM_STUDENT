package com.example.ecomarket.api;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductApi {
    private final ProductService productService;

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
}