package com.eni.bookhub.service;

import com.eni.bookhub.BO.Permission;
import com.eni.bookhub.dto.PermissionDTO;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<PermissionDTO> getPermissions();
    Optional<PermissionDTO> getPermissionById(Integer id);
    Optional<PermissionDTO> getPermissionByName(String name);
    PermissionDTO addPermission(PermissionDTO permission);
    PermissionDTO updatePermission(PermissionDTO permission);
    void deletePermission(Integer id);
}
