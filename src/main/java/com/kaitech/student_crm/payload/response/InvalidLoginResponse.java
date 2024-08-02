package com.kaitech.student_crm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class InvalidLoginResponse {
    private String username;
    private String password;



}
