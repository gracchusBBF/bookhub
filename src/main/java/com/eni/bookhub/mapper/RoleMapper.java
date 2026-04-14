package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.dto.UserRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public UserRoleDTO toDTO(UserRole role) {
        if (role == null) return null;
        return new UserRoleDTO(
                role.getId(),
                role.getRoleName()
        );
    }

    public UserRole toEntity(UserRoleDTO dto) {
        if (dto == null) return null;
        return UserRole.builder()
                .id(dto.id())
                .roleName(dto.roleName())
                .build();
    }
}
