package com.kaitech.student_crm.payload.request;


import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Data
public class LoginRequest {

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

}
