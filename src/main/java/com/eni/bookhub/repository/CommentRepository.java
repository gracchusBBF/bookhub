package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    List<Comment> findByUserId(Integer id);
    List<Comment> findByBookId(Integer id);
    List<Comment> findByStatus(String status);
}
