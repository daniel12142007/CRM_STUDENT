package com.example.ecomarket.api.admin;

import com.example.ecomarket.dto.response.CategoryResponse;
import com.example.ecomarket.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/category")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class CategoryAdminApi {
    private final CategoryService categoryService;

    @PostMapping("save/category")
    public List<CategoryResponse> saveCategory(
            @RequestParam String title,
            @RequestParam String image
    ) {
        return categoryService.saveCategory(image, title);
    }
}