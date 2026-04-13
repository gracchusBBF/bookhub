package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.BO.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByEmail(String email);
    List<User> getAll();
    Optional<List<User>> getByRole(String role);
    User save(User user);
    //oid updateUser(User user);
    void delete(int userId);
}
