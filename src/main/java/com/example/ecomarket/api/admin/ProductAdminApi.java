package com.example.ecomarket.api.admin;

import com.example.ecomarket.dto.request.ProductRequest;
import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/product")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ProductAdminApi {
    private final ProductService productService;

    @PostMapping("save/product")
    public ProductResponse save(
            @RequestBody ProductRequest request,
            @RequestParam Long categoryId) {
        return productService.saveProduct(request, categoryId);
    }
}