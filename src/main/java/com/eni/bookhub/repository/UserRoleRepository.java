package com.eni.bookhub.repository;

import com.eni.bookhub.BO.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    public Optional<UserRole> getUserRoleByName(String role);
    public Optional<UserRole>  getUserRoleById(Long id);
    public List<UserRole> getAllUserRole();
}
