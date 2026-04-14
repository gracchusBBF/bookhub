package com.eni.bookhub.dto;

import com.eni.bookhub.BO.UserRole;

public record UserDTO(
        Integer id,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        UserRole userRole
) {}