package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Comment;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.CommentCreateDTO;
import com.eni.bookhub.dto.CommentDTO;
import com.eni.bookhub.mapper.CommentMapper;
import com.eni.bookhub.repository.BookRepository;
import com.eni.bookhub.repository.CommentRepository;
import com.eni.bookhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public CommentDTO saveComment(CommentCreateDTO comment) {

        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("l'utisateur n'a pas été trouvé : " + comment.getUserId()));

        Book book = bookRepository.findById(comment.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("le livre n'a pas été trouvé : " + comment.getBookId()));

        Comment saved = commentRepository.save(CommentMapper.toEntity(comment, user, book));

        return CommentMapper.toDTO(saved);
    }

    @Override
    public CommentDTO updateStatus(int commentId, String status) {

        List<String> allowedStatus = List.of("approuved", "refused", "pending");
        if (!allowedStatus.contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "statut invalide : " + status + ". valeurs autorisées : " + allowedStatus
            );
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire non trouvé : " + commentId));

        comment.setStatus(status);
        return CommentMapper.toDTO(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> getByBookId(Integer bookId) {

        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("le livre n'a pas été trouvé : " + bookId));

        return commentRepository.findByBookId(bookId)
                .stream()
                .map(CommentMapper::toDTO)
                .toList();
    }

    @Override
    public List<CommentDTO> getByStatus(String status) {

        return commentRepository.findByStatus(status)
                .stream()
                .map(CommentMapper::toDTO)
                .toList();
    }

    @Override
    public List<CommentDTO> getByUserId(Integer userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("l'utilisateur n'a pas été trouvé : " + userId));

        return commentRepository.findByUserId(userId)
                .stream()
                .map(CommentMapper::toDTO)
                .toList();
    }

    @Override
    public List<CommentDTO> getAllComments() {

        return commentRepository.findAll()
                .stream()
                .map(CommentMapper::toDTO)
                .toList();
    }
}