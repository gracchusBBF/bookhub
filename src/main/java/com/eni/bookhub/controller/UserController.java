package com.eni.bookhub.controller;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.ChangePasswordDTO;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request) {
        try {
            // Logique : trouver l'user par mail, vérifier l'ancien pass, sauvegarder le nouveau
            userService.updatePassword(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> usersList = userService.getAll();
        System.out.println("Users ???? " + usersList);
        try {

            if (usersList.isEmpty() || usersList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(usersList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        try{
            final int userId = Integer.parseInt(id);
            final Optional<UserDTO> user = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        try{
            final Optional<UserDTO> user = userService.getUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable("role") String role) {
        try{
            final Optional<List<UserDTO>> usersByRole = userService.getByRole(role);
            return ResponseEntity.status(HttpStatus.OK).body(usersByRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO user) {
        try{
            if(user != null && user.id() == null) {
                userService.saveUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Les champs d'utilisateur ne sont pas correctement remplis.");
            }
        } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO user) {
        try{
            if(user == null || user.id() == null || user.id() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User et l'id sont obligatoires.");
            }
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {
            final int userId = Integer.parseInt(id);
            userService.delete(userId);
            return ResponseEntity.status(HttpStatus.OK).body("User " + userId + " a été supprimé.");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

}
