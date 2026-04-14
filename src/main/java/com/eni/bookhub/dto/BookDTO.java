package com.eni.bookhub.dto;

public record BookDTO(
        Integer id,
        String title,
        String lastName,
        String firstName,
        String isbn,
        String category,
        String status,
        String frontCoverImg,
        Integer copyNumber
) {}