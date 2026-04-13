package com.eni.bookhub.service;

import com.eni.bookhub.BO.Permission;
import com.eni.bookhub.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> getPermissionById(Integer id) {
        return permissionRepository.findPermissionById(id);
    }

    @Override
    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.findPermissionByPermissionName(name);
    }

    @Override
    public Permission addPermission(Permission permission) {
        System.out.println("permission : " + permission);
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Integer id) {
        permissionRepository.deleteById(id);
    }
}
