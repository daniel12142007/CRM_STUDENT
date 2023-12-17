package com.example.ecomarket.service;

import com.example.ecomarket.model.OrderItem;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.BasketRepository;
import com.example.ecomarket.repository.OrderItemRepository;
import com.example.ecomarket.repository.OrderRepository;
import com.example.ecomarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;

//    public String save(Long productId, String email) {
//        Product product = productRepository.findById(productId).get();
//        OrderItem orderItem = new OrderItem();
//        orderItem.setQuantity(1);
//        orderItem.setProduct(product);
//        orderItem.setBasket(basketRepository.findByBasteByUserId(Long.valueOf(email)));
//        orderItemRepository.save(orderItem);
//        return "hello";
//    }
}