package com.eni.bookhub.controller;

import com.eni.bookhub.dto.UserRoleDTO;
import com.eni.bookhub.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Gestion des rôles (admin, user, librarian) et de leur attribution aux utilisateurs")
public class UserRoleController {
    private UserRoleService userRoleService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userRoleService.getAllUserRoles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getUserRoleByName(@PathVariable("name") String name) {
        try{
            final Optional<UserRoleDTO> userRole = userRoleService.getUserRoleByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable("id") Integer id) {
        try{
            final Optional<UserRoleDTO> userRole = userRoleService.getUserRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
