package com.eni.bookhub.dto;

public record UserDTO(
        Integer id,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        String roleName
) {}