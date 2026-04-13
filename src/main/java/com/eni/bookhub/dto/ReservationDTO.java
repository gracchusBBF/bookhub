package com.eni.bookhub.dto;

public record ReservationDTO(
        int id,
        String status,
        Integer userId,
        Integer bookId,
        String bookTitle
) {}