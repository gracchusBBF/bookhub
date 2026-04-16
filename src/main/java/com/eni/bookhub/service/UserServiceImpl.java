package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.ChangePasswordDTO;
import com.eni.bookhub.dto.UpdateRoleUserDTO;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.dto.UserRoleDTO;
import com.eni.bookhub.dto.*;
import com.eni.bookhub.mapper.UserMapper;
import com.eni.bookhub.repository.UserRepository;
import com.eni.bookhub.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor; // Utilise RequiredArgsConstructor pour les champs final
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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
    public UserDetailsDTO getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        return dto;
    }

    @Override
    @Transactional
    public void partialUpdate(String email, UserUpdateDTO dto) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getFirstName() != null) existingUser.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existingUser.setLastName(dto.getLastName());

        userRepository.save(existingUser);
    }

    @Override
    public Optional<UserDTO> getUserById(Integer userId) {
        return userRepository.findById(userId).map(userMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).map(userMapper::toDTO);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<List<UserDTO>> getByRole(String role) {
        return userRepository.findByUserRole(role)
                .map(list -> list.stream().map(userMapper::toDTO).toList());
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RuntimeException("Informations de l'utilisateur incomplètes");
        }
        User user = userMapper.toEntity(userDTO);
        // On encode le mot de passe avant de sauvegarder si c'est une création
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(int userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    @Transactional
    public void updatePassword(ChangePasswordDTO dto) {
        User existingUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(dto.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect");
        }

        existingUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
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

    @Override
    @Transactional
    public void deleteAccount(DeleteAccountDTO userDto) {
        // 1. On cherche l'utilisateur
        User userEntity = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérification du mot de passe
        if (!passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect : suppression impossible.");
        }

        // 3. NOUVEAU : Vérifier si des emprunts sont encore en cours
        // On parcourt la liste des loans et on cherche s'il y en a un sans date de retour
        boolean hasActiveLoans = userEntity.getLoans().stream()
                .anyMatch(loan -> loan.getReturnDate() == null);

        if (hasActiveLoans) {
            throw new RuntimeException("Impossible d'anonymiser le compte : des livres n'ont pas encore été rendus.");
        }

        // 4. Si c'est bon, on anonymise
        userEntity.setEmail("deleted_" + userEntity.getId() + "@deleted.local");
        userEntity.setPhoneNumber("0000000000"); // respecter la contrainte de longueur 10
        userEntity.setPassword(passwordEncoder.encode("DELETED_USER_PWD")); // Sécurité : mot de passe bidon encodé
        userEntity.setLastName("Deleted");
        userEntity.setFirstName("User");

        // 5. On sauvegarde
        userRepository.save(userEntity);
    }
    // J'ai fusionné updateUserDetails avec la logique de retour DTO si nécessaire
    @Override
    @Transactional
    public void updateUserDetails(UserUpdateDTO dto) {
        // Appelle simplement la logique existante pour éviter la duplication
        this.partialUpdate(dto.getEmail(), dto);
    }
}