package com.eni.bookhub.service;

import com.eni.bookhub.BO.Permission;
import com.eni.bookhub.dto.PermissionDTO;
import com.eni.bookhub.mapper.PermissionMapper;
import com.eni.bookhub.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDTO> getPermissions() {

        return permissionRepository
                .findAll()
                .stream()
                .map(permissionMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<PermissionDTO> getPermissionById(Integer id) {

        return permissionRepository
                .findPermissionById(id)
                .map(permissionMapper::toDTO);
    }

    @Override
    public Optional<PermissionDTO> getPermissionByName(String name) {
        return permissionRepository
                .findPermissionByPermissionName(name)
                .map(permissionMapper::toDTO);
    }

    @Override
    public PermissionDTO addPermission(PermissionDTO permissionDTO) {
        Permission permission= permissionMapper.toEntity(permissionDTO);
        return permissionMapper.toDTO(permissionRepository.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO) {
        Permission permission= permissionMapper.toEntity(permissionDTO);
        return permissionMapper.toDTO(permissionRepository.save(permission));
    }

    @Override
    public void deletePermission(Integer id) {
        permissionRepository.deleteById(id);
    }
}
