package com.eni.bookhub.repository;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.BO.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> getUserById(Integer id);

    Optional<User> getUserByEmail(String email);

    List<User> findAll();

    Optional<List<User>> findByRole(String role);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int userId);
}
