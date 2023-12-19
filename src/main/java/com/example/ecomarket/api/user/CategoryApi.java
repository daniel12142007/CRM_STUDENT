package com.example.ecomarket.api.user;

import com.example.ecomarket.dto.response.CategoryResponse;
import com.example.ecomarket.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryApi {
    private final CategoryService categoryService;

    @GetMapping("find/all/category")
    @Operation(summary = "find all category", description ="Сategories contain a name and image")
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }
}