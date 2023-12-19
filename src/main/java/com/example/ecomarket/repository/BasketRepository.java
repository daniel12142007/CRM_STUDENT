package com.example.ecomarket.repository;

import com.example.ecomarket.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query("select b from Basket b where b.user.email = :email")
    Basket findByBasteByUserEmail(@Param(value = "email") String email);
}