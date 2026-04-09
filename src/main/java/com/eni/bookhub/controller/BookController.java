package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getBooks();
        if(books == null || books.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") String id) {
        try {
            final int bookId = Integer.parseInt(id);
            final Optional<Book> book = bookService.getBookById(bookId);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());

        }
    }

    @PostMapping("")
    public ResponseEntity<?> addBookToLibrary(@RequestBody Book book) {
        try {
            bookService.addBook(book);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
