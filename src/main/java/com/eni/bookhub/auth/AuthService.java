package com.eni.bookhub.auth;

import com.eni.bookhub.dto.AuthRequest;
import com.eni.bookhub.dto.AuthResponse;
import com.eni.bookhub.dto.RegisterRequest;
import com.eni.bookhub.repository.UserRepository;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // ← encodage du mdp
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userRole(request.getRole())
                .build();
        userRepository.save(user);

        String role = "ROLE_" + user.getUserRole().getRoleName();
        var jwt = jwtService.generateToken(user.getEmail(), role);

        return AuthResponse.builder().token(jwt).build();
    }

    public AuthResponse authenticate (AuthRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String role = user.getAuthorities().iterator().next().getAuthority();
        var jwt = jwtService.generateToken(user.getUsername(), role);

        return AuthResponse.builder().token(jwt).build();
    }

}