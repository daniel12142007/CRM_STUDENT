package com.kaitech.student_crm.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ServicesRequest(
        @NotNull(message = "Title cannot be null")
        @NotBlank(message = "Title cannot be blank") String title,
        @NotNull(message = "Description cannot be null")
        @NotBlank(message = "Description cannot be blank") String description,
        @NotNull(message = "Price cannot be null")
        @Min(value = 0, message = "The price cannot be negative") Integer price) {
}