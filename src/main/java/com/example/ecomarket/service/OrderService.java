package com.example.ecomarket.service;

import com.example.ecomarket.dto.request.OrderRequest;
import com.example.ecomarket.dto.response.*;
import com.example.ecomarket.model.Order;
import com.example.ecomarket.model.OrderItem;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.BasketRepository;
import com.example.ecomarket.repository.OrderItemRepository;
import com.example.ecomarket.repository.OrderRepository;
import com.example.ecomarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public OrderResponse order(OrderRequest request, String email) {
        Order order = Order.builder()
                .basket(basketRepository.findByBasteByUserEmail(email))
                .comments(request.getComments())
                .referencePoint(request.getReferencePoint())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .deliveryPrice(150)
                .price(productRepository.priceCount(email))
                .build();
        orderRepository.save(order);
        orderItemRepository.findAllItemsByEmail(email)
                .forEach(orderItem -> {
                    orderItem.setOrders(order);
                    orderItemRepository.save(orderItem);
                });
        orderItemRepository.findAllItemsByEmail(email)
                .forEach(orderItem -> {
                    orderItem.setBasket(null);
                    orderItemRepository.save(orderItem);
                });
        return response(order);
    }

    public OrderHisResponse historyFindById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(RuntimeException::new);
        return OrderHisResponse.builder()
                .id(order.getId())
                .data(order.getDate())
                .price(order.getPrice())
                .productResponses(productRepository.findAllProductResponseById(orderId))
                .address(order.getAddress())
                .comments(order.getComments())
                .phoneNumber(order.getPhoneNumber())
                .deliveryPrice(order.getDeliveryPrice())
                .referencePoint(order.getReferencePoint())
                .build();
    }

    public List<HistoryResponse> findAllHistory(String email) {
        return orderRepository.findAllSortByEmail(email).stream()
                .map(order -> {
                    HistoryResponse historyResponse = new HistoryResponse();
                    historyResponse.setData(order.getDate());
                    List<OrderHistoryResponse> list = orderRepository.findAllByData(order.getDate(), email).stream()
                            .map(allByDatum -> {
                                OrderHistoryResponse response = new OrderHistoryResponse();
                                response.setId(allByDatum.getId());
                                response.setData(LocalTime.of(allByDatum.getDate().getHour(), allByDatum.getDate().getMinute()));
                                response.setPrice(allByDatum.getPrice());
                                return response;
                            })
                            .collect(Collectors.toList());
                    historyResponse.setOrderHistoryResponses(list);
                    return historyResponse;
                })
                .collect(Collectors.toList());
    }

    private OrderResponse response(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .data(order.getDate())
                .price(order.getPrice())
                .address(order.getAddress())
                .comments(order.getComments())
                .phoneNumber(order.getPhoneNumber())
                .deliveryPrice(order.getDeliveryPrice())
                .referencePoint(order.getReferencePoint())
                .build();
    }
}