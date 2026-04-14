package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.Permission;
import com.eni.bookhub.dto.PermissionDTO;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public PermissionDTO toDTO(Permission permission) {
        if (permission == null) return null;
        return new PermissionDTO(
                permission.getId(),
                permission.getPermissionName()
        );
    }

    public Permission toEntity(PermissionDTO dto) {
        if (dto == null) return null;
        return Permission.builder()
                .id(dto.id())
                .permissionName(dto.permissionName())
                .build();
    }
}
