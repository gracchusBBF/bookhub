package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.ChangePasswordDTO;
import com.eni.bookhub.dto.UpdateRoleUserDTO;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.dto.UserRoleDTO;
import com.eni.bookhub.mapper.UserMapper;
import com.eni.bookhub.repository.UserRepository;
import com.eni.bookhub.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRoleRepository = userRoleRepository;
    }


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

//    @Override
//    public Optional<List<UserDTO>> getByRole(String role) {
//        return userRepository.findByUserRole(role)
//                .map(list -> list.stream()
//                        .map(userMapper::toDTO)
//                        .toList());
//    }

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

    @Override
    @Transactional
    public void delete(int userId) {
        userRepository.deleteUserById(userId);
    }
    @Override
    public void updatePassword(ChangePasswordDTO dto) {
        // 1. Récupérer l'utilisateur complet en base
        User existingUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Optionnel : Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(dto.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect");
        }

        // 3. Mettre à jour le mot de passe (avec encodage !)
        existingUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        // 4. Sauvegarder l'entité complète (l'ID est déjà dedans, donc c'est un UPDATE)
        userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void updateRole(int userId, int roleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        UserRole role = userRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        user.setUserRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUserByRoleName(String role) {
        return userRepository.findByUserRole_RoleName(role)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

}
