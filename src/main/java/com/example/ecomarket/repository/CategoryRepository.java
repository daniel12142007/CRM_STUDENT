package com.example.ecomarket.repository;

import com.example.ecomarket.dto.response.ProductResponse;
import com.example.ecomarket.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
            select
            new com.example.ecomarket.dto.response.ProductResponse(p.id,p.image,p.title,p.description,p.price,p.count)
            from Product p where p.category.id = :id
            """)
    List<ProductResponse> findAllProductByCategoryId(@Param(value = "id") Long id);
}
//private Long id;
//    private String image;
//    private String title;
//    private String description;
//    private int price;
//    private int count;