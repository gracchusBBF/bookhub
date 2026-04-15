package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUserById(Integer id);
    Optional<UserDTO> getUserByEmail(String email);
    List<UserDTO> getAll();
    Optional<List<UserDTO>> getByRole(String role);
    UserDTO saveUser(UserDTO user);
    void delete(int userId);
    void updatePassword(ChangePasswordDTO dto);
    void deleteAccount(DeleteAccountDTO user);
    void partialUpdate(String email, UserUpdateDTO dto);
    UserDetailsDTO getUserDetails(String email);
    void updateUserDetails(UserUpdateDTO dto);
}
