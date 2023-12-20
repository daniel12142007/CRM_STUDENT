package com.example.ecomarket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String phoneNumber;
    private String address;
    private String referencePoint;
    private String comments;
}