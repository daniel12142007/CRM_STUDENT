package com.example.ecomarket.service;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.model.OrderItem;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.BasketRepository;
import com.example.ecomarket.repository.OrderItemRepository;
import com.example.ecomarket.repository.OrderRepository;
import com.example.ecomarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final ModelMapper modelMapper;

    public ProductResponse saveProductOnOrder(Long productId, String email) {
        Product product = productRepository.findById(productId).orElseThrow();
        OrderItem orderItem = OrderItem.builder()
                .quantity(1)
                .product(product)
                .basket(basketRepository.findByBasteByUserEmail(email))
                .build();
        orderItemRepository.save(orderItem);
        product.setCount(1);
        return modelMapper.map(product, ProductResponse.class);
    }
}