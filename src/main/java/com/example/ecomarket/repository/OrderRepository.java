package com.example.ecomarket.repository;

import com.example.ecomarket.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.basket.user.email = :email order by o.date")
    List<Order> findAllSortByEmail(@Param(value = "email") String email);

    @Query("select o from Order o where o.date = :data and o.basket.user.email = :email")
    List<Order> findAllByData(
            @Param(value = "data") LocalDateTime localDateTime,
            @Param(value = "email") String email);
}