package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Comment;
import com.eni.bookhub.dto.CommentCreateDTO;
import com.eni.bookhub.dto.CommentDTO;
import com.eni.bookhub.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAll() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(dto));
    }

    @PatchMapping("/{commentId}/status")
    public ResponseEntity<CommentDTO> updateStatus(@PathVariable int commentId, @RequestParam String value) {
        return ResponseEntity.ok(commentService.updateStatus(commentId, value));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<CommentDTO>> getByBook(@PathVariable int bookId) {
        return ResponseEntity.ok(commentService.getByBookId(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(commentService.getByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CommentDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(commentService.getByStatus(status));
    }











}
