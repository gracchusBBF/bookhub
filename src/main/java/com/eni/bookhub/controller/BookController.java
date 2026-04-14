package com.eni.bookhub.controller;

import com.eni.bookhub.dto.BookDTO;
import com.eni.bookhub.exception.BookNotFoundException;
import com.eni.bookhub.exception.DuplicateIsbnException;
import com.eni.bookhub.exception.InvalidBookIdException;
import com.eni.bookhub.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @ExceptionHandler(InvalidBookIdException.class)
    public ResponseEntity<String> handleInvalidId(InvalidBookIdException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(BookNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<String> handleDuplicateIsbn(DuplicateIsbnException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @GetMapping
    public ResponseEntity<Optional<List<BookDTO>>> getAllBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {

        Optional<List<BookDTO>> books;

        if ((category != null && !category.isBlank()) || (status != null && !status.isBlank())) {
            books = bookService.filterBooks(page, category, status);
        } else {
            books = Optional.ofNullable(bookService.getBooks(page));
        }

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooksByQuery(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String query = requestParams.get("query");

        int pageNum = (page != null) ? Integer.parseInt(page) : 1;

        Optional<List<BookDTO>> books =  bookService.searchBooks(pageNum, query);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        BookDTO book = getValidateBookDTO(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<?> addBookToLibrary(@Valid @RequestBody BookDTO book) {
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookFromLibrary(@PathVariable String id){
        BookDTO book = getValidateBookDTO(id);
        bookService.deleteBook(book.id());
        return ResponseEntity.ok("Le livre avec l'ID " + book.id() + " a bien été supprimé");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable String id,
            @Valid @RequestBody BookDTO bookUpdates) {

        BookDTO existingBook = getValidateBookDTO(id);
        mergeBookDTO(existingBook, bookUpdates);
        return ResponseEntity.ok(bookService.updateBook(existingBook));
    }

    private BookDTO getValidateBookDTO(String id) {
        int bookId;
        try {
            bookId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new InvalidBookIdException(id);
        }
        if (bookId <= 0) {
            throw new InvalidBookIdException(id);
        }

        return bookService.getBookById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    private void mergeBookDTO(BookDTO existingBook, BookDTO bookUpdates) {
        new BookDTO(
                existingBook.id(),
                bookUpdates.title() != null ? bookUpdates.title() : existingBook.title(),
                bookUpdates.lastName() != null ? bookUpdates.lastName() : existingBook.lastName(),
                bookUpdates.firstName() != null ? bookUpdates.firstName() : existingBook.firstName(),
                existingBook.isbn(),
                bookUpdates.category() != null ? bookUpdates.category() : existingBook.category(),
                bookUpdates.status() != null ? bookUpdates.status() : existingBook.status(),
                bookUpdates.frontCoverImg() != null ? bookUpdates.frontCoverImg() : existingBook.frontCoverImg(),
                bookUpdates.copyNumber() != null ? bookUpdates.copyNumber() : existingBook.copyNumber()
        );
    }
}
