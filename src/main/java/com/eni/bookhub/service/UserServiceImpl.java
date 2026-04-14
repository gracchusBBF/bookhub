package com.eni.bookhub.service;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.ChangePasswordDTO;
import com.eni.bookhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
