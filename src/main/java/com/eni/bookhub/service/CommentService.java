package com.eni.bookhub.service;

import com.eni.bookhub.BO.Comment;
import com.eni.bookhub.dto.CommentCreateDTO;
import com.eni.bookhub.dto.CommentDTO;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CommentService {

    public CommentDTO saveComment(CommentCreateDTO comment);
    public CommentDTO updateStatus(int commentId, String status);
    public List<CommentDTO> getByBookId(Integer id);
    public List<CommentDTO> getByStatus(String status);
    public List<CommentDTO> getByUserId(Integer id);
    public List<CommentDTO> getAllComments();

}