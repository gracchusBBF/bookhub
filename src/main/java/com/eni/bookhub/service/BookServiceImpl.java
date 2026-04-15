package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.dto.BookDTO;
import com.eni.bookhub.dto.PageResponseDTO;
import com.eni.bookhub.exception.DuplicateIsbnException;
import com.eni.bookhub.mapper.BookMapper;
import com.eni.bookhub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @Override
    public PageResponseDTO<BookDTO> getBooks(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, 20);
        Page<Book> page = bookRepository.findAll(pageable);

        List<BookDTO> content = page.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                page.getTotalPages(),
                page.getTotalElements(),
                pageNum
        );
    }

    @Override
    public Optional<BookDTO> getBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toDTO);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        if (bookDTO == null) {
            throw new RuntimeException("Informations du livre incomplètes");
        }
        if (bookRepository.existsBooksByIsbn(bookDTO.isbn())) {
            throw new DuplicateIsbnException(bookDTO.isbn());
        }
        Book book = bookMapper.toEntity(bookDTO);
        try {
            return bookMapper.toDTO(bookRepository.save(book));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de sauvegarder ce livre" + e.getMessage());
        }
    }

    @Override
    public Optional<BookDTO> getBookByTitle(String title) {
        return bookRepository.findBooksByTitle(title)
                .map(bookMapper::toDTO);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Integer bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Optional<List<BookDTO>> searchBooks(int pageNum, String query) {
        Pageable pageable = PageRequest.of(pageNum - 1, 20);
        return Optional.of(
                bookRepository.searchBooksByQuery(query, pageable)
                        .getContent()
                        .stream()
                        .map(bookMapper::toDTO)
                        .toList()
        );
    }

    @Override
    public Optional<List<BookDTO>> filterBooks(int pageNum, String category, String status) {
        Pageable pageable = PageRequest.of(pageNum - 1, 20);
        String cat = (category != null && !category.isBlank()) ? category : null;
        String sta = (status != null && !status.isBlank()) ? status : null;
        return Optional.of(
                bookRepository.getBooksByFilters(cat, sta, pageable)
                        .getContent()
                        .stream()
                        .map(bookMapper::toDTO)
                        .toList()
        );
    }
}