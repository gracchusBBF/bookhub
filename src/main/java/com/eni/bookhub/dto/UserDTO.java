package com.eni.bookhub.dto;

import com.eni.bookhub.BO.Reservation;

import java.util.List;

public record UserDTO(
        Integer id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        UserRoleDTO userRole,
        List<LoanDTO> loans,
        List<ReservationDTO> reservations,
        List<CommentDTO> comments
) {}