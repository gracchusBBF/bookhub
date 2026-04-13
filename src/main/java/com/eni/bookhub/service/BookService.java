package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBooks(int pageNum);
    Optional<Book> getBookById(Integer bookId);
    void addBook(Book book);

    Optional<Book> getBookByTitle(String title);

    Book updateBook(Book book);
    void deleteBook(Integer bookId);

    Optional<List<Book>> searchBooks(int pageNum, String query);

    Optional<List<Book>> filterBooks(int pageNum, String category, String status);
}
