package com.example.ecomarket.service;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.repository.CategoryRepository;
import com.example.ecomarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> searchProductByTitleAndCategoryId(Long id) {
        return productRepository.findAllProductByCategoryId(id);
    }

    public List<ProductResponse> searchProductByTitle(String title) {
        return productRepository.searchProductByTitle(title);
    }

    public List<ProductResponse> searchProductByTitleAndCategoryId(String title, Long categoryId) {
        return productRepository.searchProductByTitleAndByCategoryId(title, categoryId);
    }
}