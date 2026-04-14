package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Permission;
import com.eni.bookhub.dto.PermissionDTO;
import com.eni.bookhub.exception.InvalidBookIdException;
import com.eni.bookhub.exception.PermissionNotFoundException;
import com.eni.bookhub.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private PermissionService permissionService;

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(PermissionNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> findAllPermissions() {
        try {
            List<PermissionDTO> permissions = permissionService.getPermissions();
            return ResponseEntity.ok(permissions);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Object> findPermissionByName(@PathVariable String name) {
        try {
            if(name == null){
                return ResponseEntity.badRequest().build();
            }

            Optional<PermissionDTO> permission = permissionService.getPermissionByName(name);
            if(permission.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(permission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findPermissionById(@PathVariable String id) {
        try {
            if(id == null){
            return ResponseEntity.badRequest().build();
        }
            int pId = Integer.parseInt(id);

            Optional<PermissionDTO> permission = permissionService.getPermissionById(pId);
            if(permission.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(permission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public  ResponseEntity<?> addNewPermission(@Valid @RequestBody PermissionDTO permission) {
        try {
            String permissionName = permission.permissionName();
            Optional<PermissionDTO> permissionsDB = permissionService.getPermissionByName(permissionName);
            if(permissionsDB.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Permission '" + permissionName + "' already exists.");
            }

            PermissionDTO newPermission = permissionService.addPermission(permission);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(
            @PathVariable String id,
            @Valid @RequestBody PermissionDTO permissionUpdates) {

        PermissionDTO existingPermission = getValidatePermission(id);
        updatePermissionDTO(existingPermission, permissionUpdates);
        return ResponseEntity.ok(permissionService.updatePermission(existingPermission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable String id){
        try {
            int idPermission = Integer.parseInt(id);

            Optional<PermissionDTO> permissionDB = permissionService.getPermissionById(idPermission);

            if(permissionDB.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            permissionService.deletePermission(idPermission);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private PermissionDTO getValidatePermission(String id) {
        int permissionId;
        try {
            permissionId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new InvalidBookIdException(id);
        }
        if (permissionId <= 0) {
            throw new InvalidBookIdException(id);
        }

        return permissionService.getPermissionById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(permissionId));
    }

    private void updatePermissionDTO(PermissionDTO existingPermission, PermissionDTO permissionUpdates) {
        new PermissionDTO(
                existingPermission.id(),
                permissionUpdates.permissionName() != null ? permissionUpdates.permissionName() : existingPermission.permissionName()
        );
    }
}
