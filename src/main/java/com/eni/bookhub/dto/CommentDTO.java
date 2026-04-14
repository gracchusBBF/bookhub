package com.eni.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private int id;
    private int rate;
    private String comment;
    private String status;

    private int userId;
    private String userFirstName;
    private String userLastName;

    private int bookId;
    private String bookTitle;

}