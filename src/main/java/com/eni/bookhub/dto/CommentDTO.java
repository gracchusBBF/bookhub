package com.eni.bookhub.dto;

public record CommentDTO(
        int id,
        int rate,
        String comment,
        String status,
        String authorName, // "FirstName LastName"
        Integer bookId
) {}