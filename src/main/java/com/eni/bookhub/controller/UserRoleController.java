package com.eni.bookhub.controller;

import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {
    private UserRoleService userRoleService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userRoleService.getAllUserRole());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getUserRoleByName(@PathVariable("name") String name) {
        try{
            final Optional<UserRole> userRole = userRoleService.getUserRoleByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable("id") Long id) {
        try{
            final Optional<UserRole> userRole = userRoleService.getUserRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
