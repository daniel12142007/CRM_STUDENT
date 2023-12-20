package com.example.ecomarket.repository;

import com.example.ecomarket.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
            select o
            from OrderItem o
            where o.product.id = :productId
            and o.basket.user.email = :email
            """)
    OrderItem findOrderItemByProductId(
            @Param(value = "productId") Long productId,
            @Param(value = "email") String email
    );
}