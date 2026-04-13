package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<List<User>> getByRole(String role) {
        return userRepository.findByUserRole(role);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(int userId) {
        userRepository.deleteUserById(userId);
    }
}
