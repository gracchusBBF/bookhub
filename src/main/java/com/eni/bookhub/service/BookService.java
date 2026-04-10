package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBooks();
    Optional<Book> getBookById(Integer bookId);
    void addBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Integer bookId);
}
