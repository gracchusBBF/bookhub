package com.eni.bookhub.repository;

import com.eni.bookhub.BO.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findById(Integer id);

    Optional<User> getUserByEmail(String email);

    List<User> findAll();

    Optional<List<User>> findByUserRole(String role);

    User save(User user);

    void deleteUserById(int userId);

    Optional<User> findByEmail(String email);
}
