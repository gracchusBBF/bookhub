package com.eni.bookhub.dto;

import com.eni.bookhub.BO.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private UserRole role;
    private String phoneNumber;
    private String firstName;
    private String lastName;

}