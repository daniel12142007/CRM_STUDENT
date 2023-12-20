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
import org.springframework.security.core.parameters.P;
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

    public ProductResponse plusOne(String email, Long productId) {
        return one(email, productId, 1);
    }

    public ProductResponse minusOne(String email, Long productId) {
        return one(email, productId, 0);
    }

    private ProductResponse one(String email, Long productId, int a) {
        try {
            OrderItem orderItem = orderItemRepository.findOrderItemByProductId(productId, email);
            Product product = orderItem.getProduct();
            if (a == 1) {
                orderItem.setQuantity(orderItem.getQuantity() + 1);
            } else {
                orderItem.setQuantity(Math.max((orderItem.getQuantity() - 1), 1));
            }
            orderItemRepository.save(orderItem);
            return ProductResponse.builder()
                    .id(product.getId())
                    .image(product.getImage())
                    .title(product.getTitle())
                    .count(orderItem.getQuantity())
                    .description(product.getDescription())
                    .price(orderItem.getQuantity() * product.getPrice())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("You have not added this product to your cart!");
        }
    }
}