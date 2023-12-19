package com.example.ecomarket.repository;

import com.example.ecomarket.dto.response.CategoryResponse;
import com.example.ecomarket.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
            select 
            new com.example.ecomarket.dto.response.CategoryResponse(c.id,c.title,c.image)
            from Category c
            """)
    List<CategoryResponse> findAllCategoryResponse();
}