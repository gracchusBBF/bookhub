package com.eni.bookhub.service;

import com.eni.bookhub.BO.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    Optional<UserRole> getUserRoleByName(String role);
    Optional<UserRole> getUserRoleById(Long id);
    List<UserRole> getAllUserRoles();
}
