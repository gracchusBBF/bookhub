package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserRoleDTO roleDTO = user.getUserRole() != null
                ? new UserRoleDTO(user.getUserRole().getId(), user.getUserRole().getRoleName())
                : null;

        List<LoanDTO> loansDTO = user.getLoans() != null
                ? user.getLoans().stream()
                .map(loan -> new LoanDTO(
                        loan.getId(),
                        loan.getStartDate(),
                        loan.getDeadline(),
                        loan.getReturnDate(),
                        user.getEmail(),
                        loan.getBook() != null ? loan.getBook().getId() : null,
                        loan.getBook() != null ? loan.getBook().getTitle() : null
                ))
                .toList()
                : new ArrayList<>();

        List<ReservationDTO> reservationDTO = (user.getReservations() != null)
                ? user.getReservations().stream()
                .map(res -> new ReservationDTO(
                        res.getId(),
                        res.getStatus(),
                        user.getId(),
                        res.getBook().getId(),
                        res.getBook().getTitle()
                ))
                .toList()
                : List.of();

        List<CommentDTO> commentDTO = (user.getComments() != null)
                ? user.getComments().stream()
                .filter(res -> res.getUser() != null && res.getBook() != null)
                .map(res -> new CommentDTO(
                        res.getId(),
                        res.getRate(),
                        res.getComment(),
                        res.getStatus(),
                        res.getUser().getId(),
                        res.getUser().getFirstName(),
                        res.getUser().getLastName(),
                        res.getBook().getId(),
                        res.getBook().getTitle()
                ))
                .toList()
                : List.of();

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                roleDTO,
                loansDTO,
                reservationDTO,
                commentDTO
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
                .build();
    }
}
