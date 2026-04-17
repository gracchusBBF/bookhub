package com.eni.bookhub.controller;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.*;
import com.eni.bookhub.dto.ChangePasswordDTO;
import com.eni.bookhub.dto.UpdateRoleUserDTO;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.dto.UserRoleDTO;
import com.eni.bookhub.dto.UserUpdateDTO;
import com.eni.bookhub.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@Tag(name = "Users", description = "Gestion des utilisateurs") // permet d'afficher d'afficher la description du controller dans l'ui de swagger
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


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
        log.info("list user atteint");
        try {
            List<UserDTO> usersList = userService.getAll();
            if (usersList.isEmpty() || usersList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(usersList);
        } catch (Exception e) {
            e.printStackTrace(); // CELA VA ENFIN AFFICHER L'ERREUR DANS VOTRE CONSOLE
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
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
    @GetMapping("/details/{email}")
    public UserDetailsDTO getUserDetails(@PathVariable("email") String email){
        return userService.getUserDetails(email);
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

    @PutMapping("/update-details")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO user) {
        try{
            userService.updateUserDetails(user);
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

    @PutMapping("/delete-account")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody DeleteAccountDTO user){
        try {
            userService.deleteAccount(user);
            return ResponseEntity.status(HttpStatus.OK).body("Le compte a bien été supprimé.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody UserUpdateDTO dto) {
        try {
            userService.partialUpdate(email, dto);
            return ResponseEntity.status(HttpStatus.OK).body("informations mise à jour");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable int id, @RequestBody UserRoleDTO roleDTO) {
        try {
            final Optional<UserDTO> user = userService.getUserById(id);
            userService.updateRole(id, roleDTO.getId());
            return ResponseEntity.status(HttpStatus.OK).body("le statut de l'utilisateur " +  user.toString() + "a été changé pour " + roleDTO.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
