package com.example.ecomarket.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String image;
    private String title;
    private String description;
    private int price;
}