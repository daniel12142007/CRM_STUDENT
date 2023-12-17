package com.example.ecomarket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String image;
    private String title;
    private String description;
    private int price;
    private int count;
}