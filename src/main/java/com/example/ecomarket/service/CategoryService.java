package com.example.ecomarket.service;

import com.example.ecomarket.dto.response.CategoryResponse;
import com.example.ecomarket.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryResponse> findAll() {
        List<CategoryResponse> list = new ArrayList<>();
        categoryRepository.findAll().forEach(a -> {
            list.add(modelMapper.map(a, CategoryResponse.class));
        });
        return list;
    }
}