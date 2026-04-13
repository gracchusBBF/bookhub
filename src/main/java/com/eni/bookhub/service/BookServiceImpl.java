package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.exception.DuplicateIsbnException;
import com.eni.bookhub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public List<Book> getBooks(int pageNum) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNum -1, pageSize);
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public Optional<Book> getBookById(Integer bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public void addBook(Book book) {
        if(book == null) {
            throw new RuntimeException("Informations du livre incomplètes");
        }
        if(bookRepository.existsBooksByIsbn(book.getIsbn())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de sauvegarder ce livre" + e.getMessage());
        }
    }

    @Override
    public Optional<Book> getBookByTitle(String title) {
        return bookRepository.findBooksByTitle(title);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Integer bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Optional<List<Book>> searchBooks(int pageNum, String query) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNum -1, pageSize);
        return Optional.of(bookRepository.searchBooksByQuery(query, pageable).getContent());
    }

    public Optional<List<Book>> filterBooks(int pageNum, String category, String status) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNum -1, pageSize);
        String cat = (category != null && !category.isBlank()) ? category : null;
        String sta = (status != null && !status.isBlank()) ? status : null;
        return Optional.of(bookRepository.getBooksByFilters(cat, sta, pageable).getContent());
    }

}
