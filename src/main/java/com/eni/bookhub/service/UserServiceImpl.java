package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.*;
import com.eni.bookhub.mapper.UserMapper;
import com.eni.bookhub.repository.UserRepository;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = new UserMapper();
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
    public void partialUpdate(String email, UserUpdateDTO dto) {
        // 1. Récupérer l'existant ou lancer une erreur
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérification manuelle de chaque champ
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getFirstName() != null) {
            existingUser.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            existingUser.setLastName(dto.getLastName());
        }

        // 3. Sauvegarder les modifications
        userRepository.save(existingUser);
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

    @Override
    public void deleteAccount(DeleteAccountDTO userDto) {
        // 1. On cherche l'utilisateur
        userRepository.getUserByEmail(userDto.getEmail()).ifPresent(userEntity -> {
            
            // 2. Vérification du mot de passe
            // On compare le mot de passe clair du DTO avec le mot de passe encodé en base
            if (!passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
                throw new RuntimeException("Mot de passe incorrect : suppression impossible.");
            }

            // 3. Si c'est bon, on anonymise
            userEntity.setEmail("deleted_" + userEntity.getId() + "@deleted.local");
            userEntity.setPhoneNumber("deleted_" + userEntity.getId());
            userEntity.setPassword("********"); // pas de contrainte d'unicité
            userEntity.setLastName("Deleted");
            userEntity.setFirstName("User");

            // 4. On sauvegarde
            userRepository.save(userEntity);
        });
    }
    @Override
    public void updateUserDetails(UserUpdateDTO dto) {
        if (dto == null || dto.getEmail() == null) {
            throw new RuntimeException("L'email est requis pour identifier l'utilisateur");
        }

        // 1. Récupérer l'utilisateur existant
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + dto.getEmail()));

        // 2. Mise à jour partielle (Ta stratégie des "if")
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        // Si tu autorises le changement d'email, ajoute-le ici
        // (Attention : cela impactera tes futures recherches par email)
        // if (dto.getEmail() != null) { user.setEmail(dto.getEmail()); }

        // 3. Sauvegarder en base
        User savedUser = userRepository.save(user);

        // 4. Retourner le format UserDetailsDTO pour le Dashboard Angular
        UserDetailsDTO response = new UserDetailsDTO();
        response.setEmail(savedUser.getEmail());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setPhoneNumber(savedUser.getPhoneNumber());


    }
}
