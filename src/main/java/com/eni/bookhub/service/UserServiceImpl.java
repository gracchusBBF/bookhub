package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserServiceImpl {
    private UserRepository userRepository;

    public Optional<User> getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<List<User>> getByRole(String role) {
        return userRepository.findByRole(role);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }
}
