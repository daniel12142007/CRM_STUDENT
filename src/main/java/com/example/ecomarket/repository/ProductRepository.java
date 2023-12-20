package com.example.ecomarket.repository;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            select
            new com.example.ecomarket.dto.response.ProductResponse(
            p.id,
            p.image,
            p.title,
            p.description,
            p.price*coalesce(case when p.id = orderItem.product.id then orderItem.quantity else 1 end, 1) ,
            coalesce(case when p.id != orderItem.product.id or orderItem.product.id is null then p.count else orderItem.quantity end, p.count))
            from Basket b,Product p
            left join b.orderItems orderItem
            where b.user.email = :email
            and p.title like :title%
             """)
    List<ProductResponse> searchProductByTitle(
            @Param(value = "title") String title,
            @Param(value = "email") String email);

    @Query("""
            select
            new com.example.ecomarket.dto.response.ProductResponse(
            p.id,
            p.image,
            p.title,
            p.description,
            p.price*coalesce(case when p.id = orderItem.product.id then orderItem.quantity else 1 end, 1) ,
            coalesce(case when p.id != orderItem.product.id or orderItem.product.id is null then p.count else orderItem.quantity end, p.count))
            from Basket b
            left join b.orderItems orderItem
            join Product p
            on p.category.id = :categoryId
            where b.user.email = :email
            and p.title like :title%
            """)
    List<ProductResponse> searchProductByTitleAndByCategoryId(
            @Param(value = "title") String title,
            @Param(value = "categoryId") Long categoryId,
            @Param(value = "email") String email);

    @Query("""
            select
            new com.example.ecomarket.dto.response.ProductResponse(
            p.id,
            p.image,
            p.title,
            p.description,
            p.price*coalesce(case when p.id = orderItem.product.id then orderItem.quantity else 1 end, 1) ,
            coalesce(case when p.id != orderItem.product.id or orderItem.product.id is null then p.count else orderItem.quantity end, p.count))
            from Basket b
            left join b.orderItems orderItem
            join Product p
            on p.category.id = :categoryId
            where b.user.email = :email
            """)
    List<ProductResponse> findOrderItemByIdAndEmail(
            @Param(value = "email") String email,
            @Param(value = "categoryId") Long categoryId);

    @Query("""
            select
            new com.example.ecomarket.dto.response.ProductResponse(
            p.id,
            p.image,
            p.title,
            p.description,
            p.price*coalesce(case when p.id = orderItem.product.id then orderItem.quantity else 1 end, 1) ,
            coalesce(case when p.id != orderItem.product.id or orderItem.product.id is null then p.count else orderItem.quantity end, p.count))
            from Basket b,Product p
            left join b.orderItems orderItem
            where p.id = :productId
            and b.user.email = :email
            """)
    ProductResponse findByIdProduct(
            @Param(value = "email") String email,
            @Param(value = "productId") Long productId);
}