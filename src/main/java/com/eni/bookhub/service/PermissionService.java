package com.eni.bookhub.service;

import com.eni.bookhub.BO.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> getPermissions();
    Optional<Permission> getPermissionById(Integer id);
    Optional<Permission> getPermissionByName(String name);
    Permission addPermission(Permission permission);
    Permission updatePermission(Permission permission);
    void deletePermission(Integer id);
}
