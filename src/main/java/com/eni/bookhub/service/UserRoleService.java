package com.eni.bookhub.service;

import com.eni.bookhub.dto.UserRoleDTO;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    Optional<UserRoleDTO> getUserRoleByName(String role);
    Optional<UserRoleDTO> getUserRoleById(Integer id);
    List<UserRoleDTO> getAllUserRoles();
}
