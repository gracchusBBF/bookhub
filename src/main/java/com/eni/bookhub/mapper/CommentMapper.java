package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Comment;
import com.eni.bookhub.dto.CommentCreateDTO;
import com.eni.bookhub.dto.CommentDTO;
import com.eni.bookhub.BO.User;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .rate(comment.getRate())
                .comment(comment.getComment())
                .status(comment.getStatus())
                .userId(comment.getUser().getId())
                .userFirstName(comment.getUser().getFirstName())
                .userLastName(comment.getUser().getLastName())
                .bookId(comment.getBook().getId())
                .bookTitle(comment.getBook().getTitle())
                .build();
    }

    public static Comment toEntity(CommentCreateDTO dto, User user, Book book) {
        Comment comment = new Comment();
        comment.setRate(dto.getRate());
        comment.setComment(dto.getComment());
        comment.setUser(user);
        comment.setBook(book);
        return comment;
    }


}
