package com.kaitech.student_crm.payload.response;

public record ServiceItemResponse(Long id,
                                  String title,
                                  String description,
                                  Long serviceId) {
}