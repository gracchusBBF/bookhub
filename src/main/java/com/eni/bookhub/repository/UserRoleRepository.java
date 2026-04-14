package com.eni.bookhub.repository;

import com.eni.bookhub.BO.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> getUserRoleByRoleName(String role);
    Optional<UserRole>  getUserRoleById(Integer id);
    List<UserRole> findAll();
}
