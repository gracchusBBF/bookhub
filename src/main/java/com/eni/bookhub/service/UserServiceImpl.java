package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.mapper.UserMapper;
import com.eni.bookhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public Optional<UserDTO> getUserById(Integer userId) {

        return userRepository
                .findById(userId)
                .map(userMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {

        return userRepository
                .getUserByEmail(email)
                .map(userMapper::toDTO);
    }

    @Override
    public List<UserDTO> getAll() {

        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<List<UserDTO>> getByRole(String role) {
        return userRepository.findByUserRole(role)
                .map(list -> list.stream()
                        .map(userMapper::toDTO)
                        .toList());
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RuntimeException("Informations de l'utilisateur incomplètes");
        }
        User user = userMapper.toEntity(userDTO);
        try {
            return userMapper.toDTO(userRepository.save(user));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de sauvegarder cet utilisateur " + e.getMessage());
        }
    }

    public void delete(int userId) {
        userRepository.deleteUserById(userId);
    }
}
