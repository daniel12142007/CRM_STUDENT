package com.example.ecomarket.service;

import com.example.ecomarket.dto.request.ProductRequest;
import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.model.Category;
import com.example.ecomarket.model.OrderItem;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.CategoryRepository;
import com.example.ecomarket.repository.ProductRepository;
import com.example.ecomarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public List<ProductResponse> searchProductByTitleAndCategoryId(List<Long> id, String email, Long categoryId) {
        List<ProductResponse> list = new ArrayList<>();
        for (OrderItem orderItem : productRepository.findOrderItemByIdAndEmail(id, email, categoryId)) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(orderItem.getProduct().getId());
            productResponse.setCount(orderItem.getQuantity());
            list.add(productResponse);
        }
        return list;
    }

    public List<ProductResponse> searchProductByTitle(String title) {
        return productRepository.searchProductByTitle(title);
    }

    public List<ProductResponse> searchProductByTitleAndCategoryId(String title, Long categoryId) {
        return productRepository.searchProductByTitleAndByCategoryId(title, categoryId);
    }

    public ProductResponse findById(Long productId) {
        return modelMapper.map(productRepository.findById(productId).orElseThrow(), ProductResponse.class);
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
}