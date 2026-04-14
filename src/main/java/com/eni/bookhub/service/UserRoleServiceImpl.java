package com.eni.bookhub.service;

import com.eni.bookhub.dto.UserRoleDTO;
import com.eni.bookhub.mapper.RoleMapper;
import com.eni.bookhub.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {
    private UserRoleRepository userRoleRepository;
    private RoleMapper roleMapper;

    public Optional<UserRoleDTO> getUserRoleById(Integer id){
        return userRoleRepository
                .getUserRoleById(id)
                .map(roleMapper::toDTO);
    }
    public Optional<UserRoleDTO> getUserRoleByName(String name) {
        return userRoleRepository
                .getUserRoleByRoleName(name)
                .map(roleMapper::toDTO);
    }
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository
                .findAll()
                .stream()
                .map(roleMapper::toDTO)
                .toList();
    }
}
