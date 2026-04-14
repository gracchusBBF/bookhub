package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getLastName(),
                user.getFirstName(),
                user.getPhoneNumber(),
                user.getUserRole()
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.id())
                .lastName(dto.lastName())
                .firstName(dto.firstName())
                .email(dto.email())
                .phoneNumber(dto.phoneNumber())
                .userRole(dto.userRole())
                .build();
    }
}
