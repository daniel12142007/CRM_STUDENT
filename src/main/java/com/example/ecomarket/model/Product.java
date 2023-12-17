package com.example.ecomarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String title;
    private String description;
    private int price;
    private int count;
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItem;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}