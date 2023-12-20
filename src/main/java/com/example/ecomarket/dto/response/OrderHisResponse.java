package com.example.ecomarket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHisResponse {
    private Long id;
    private String phoneNumber;
    private String address;
    private String referencePoint;
    private String comments;
    private LocalDateTime data;
    private int price;
    private int deliveryPrice;
    private List<ProductResponse> productResponses;
}