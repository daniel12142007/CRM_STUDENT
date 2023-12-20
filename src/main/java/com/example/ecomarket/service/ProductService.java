package com.example.ecomarket.service;

import com.example.ecomarket.dto.request.ProductRequest;
import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.model.Category;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.CategoryRepository;
import com.example.ecomarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<ProductResponse> searchProductByTitleAndCategoryId(String email, Long categoryId) {
        return productRepository.findOrderItemByIdAndEmail(email, categoryId);
    }

    public List<ProductResponse> searchProductByTitle(String title, String email) {
        return productRepository.searchProductByTitle(title, email);
    }

    public List<ProductResponse> searchProductByTitleAndByTitle(String title, String email, Long categoryId) {
        return productRepository.searchProductByTitleAndByCategoryId(title, categoryId, email);
    }

    public ProductResponse findById(Long productId, String email) {
        return productRepository.findByIdProduct(email, productId);
    }

    public ProductResponse saveProduct(ProductRequest request, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(RuntimeException::new);
        Product product = Product.builder()
                .count(0)
                .category(category)
                .title(request.getTitle())
                .price(request.getPrice())
                .image(request.getImage())
                .description(request.getDescription())
                .build();
        productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    public Integer basketSum(String email) {
        return productRepository.priceCount(email);
    }
}