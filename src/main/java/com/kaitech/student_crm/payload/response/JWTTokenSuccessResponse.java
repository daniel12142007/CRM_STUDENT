package com.kaitech.student_crm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTTokenSuccessResponse {
    private boolean success;
    private String token;
    private String role;
}
