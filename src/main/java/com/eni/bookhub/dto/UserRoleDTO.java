package com.eni.bookhub.dto;

import java.util.List;

public record UserRoleDTO(
        Integer id,
        String roleName,
        List<String> permissions
) {}
