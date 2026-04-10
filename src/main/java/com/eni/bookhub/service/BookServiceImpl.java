package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.exception.DuplicateIsbnException;
import com.eni.bookhub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
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
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Integer bookId) {
        bookRepository.deleteById(bookId);
    }

}
