package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findPermissionByPermissionName(String name);

    Optional<Permission> findPermissionById(Integer id);
}
