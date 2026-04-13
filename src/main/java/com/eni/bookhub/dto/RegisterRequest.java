package com.eni.bookhub.dto;

import com.eni.bookhub.BO.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private UserRole role;

    public  RegisterRequest(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
