package com.example.ecomarket.service;

import com.example.ecomarket.dto.response.CategoryResponse;
import com.example.ecomarket.model.Category;
import com.example.ecomarket.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllCategoryResponse();
    }

    public List<CategoryResponse> saveCategory(String image, String title) {
        Category category = Category.builder()
                .title(title)
                .image(image)
                .build();
        categoryRepository.save(category);
        return findAll();
    }
}