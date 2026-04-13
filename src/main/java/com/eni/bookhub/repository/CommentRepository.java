package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
