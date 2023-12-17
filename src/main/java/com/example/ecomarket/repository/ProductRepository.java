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
            new com.example.ecomarket.dto.response.ProductResponse(p.id,p.image,p.title,p.description,p.price,p.count)
            from Product p where p.category.id = :id
            """)
    List<ProductResponse> findAllProductByCategoryId(@Param(value = "id") Long id);

    @Query("""
                        select
                        new com.example.ecomarket.dto.response.ProductResponse(p.id,p.image,p.title,p.description,p.price,p.count)
                        from Product p where p.title like :title%
            """)
    List<ProductResponse> searchProductByTitle(@Param(value = "title") String title);@Query("""
                        select
                        new com.example.ecomarket.dto.response.ProductResponse(p.id,p.image,p.title,p.description,p.price,p.count)
                        from Product p where p.title like :title%
                        and p.category.id = :id
            """)
    List<ProductResponse> searchProductByTitleAndByCategoryId(@Param(value = "title") String title,@Param(value = "id")Long id);
}