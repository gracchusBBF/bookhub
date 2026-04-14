package com.eni.bookhub.dto;

import com.eni.bookhub.BO.Reservation;

import java.util.List;

public record UserDTO(
        Integer id,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        UserRoleDTO userRole,
        List<LoanDTO> loans,
        List<ReservationDTO> reservations,
        List<CommentDTO> comments
) {}