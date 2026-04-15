package com.eni.bookhub.service;

import com.eni.bookhub.dto.BookDTO;
import com.eni.bookhub.dto.PageResponseDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    PageResponseDTO<BookDTO> getBooks(int pageNum);
    Optional<BookDTO> getBookById(Integer bookId);
    BookDTO addBook(BookDTO book);

    Optional<BookDTO> getBookByTitle(String title);

    BookDTO updateBook(BookDTO book);
    void deleteBook(Integer bookId);

    Optional<List<BookDTO>> searchBooks(int pageNum, String query);

    Optional<List<BookDTO>> filterBooks(int pageNum, String category, String status);
}
