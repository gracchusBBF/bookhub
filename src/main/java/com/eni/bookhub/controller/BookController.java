package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Book;
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
    public ResponseEntity<Optional<List<Book>>> getAllBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {

        Optional<List<Book>> books;

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

        Optional<List<Book>> books =  bookService.searchBooks(pageNum, query);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        Book book = getValidateBook(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<?> addBookToLibrary(@Valid @RequestBody Book book) {
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookFromLibrary(@PathVariable String id){
        Book book = getValidateBook(id);
        bookService.deleteBook(book.getId());
        return ResponseEntity.ok("Le livre avec l'ID " + book.getId() + " a bien été supprimé");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable String id,
            @Valid @RequestBody Book bookUpdates) {

        Book existingBook = getValidateBook(id);
        updateBookField(existingBook, bookUpdates);
        return ResponseEntity.ok(bookService.updateBook(existingBook));
    }

    private Book getValidateBook(String id) {
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

    private void updateBookField(Book existingBook, Book bookUpdates) {
        // Replace old with new fields for book table, without changing book_id (PK)
        // or isbn (UK) which otherwise will prevent the save method.
        if (bookUpdates.getTitle() != null) {
            existingBook.setTitle(bookUpdates.getTitle());
        }
        if (bookUpdates.getLastName() != null) {
            existingBook.setLastName(bookUpdates.getLastName());
        }
        if (bookUpdates.getFirstName() != null) {
            existingBook.setFirstName(bookUpdates.getFirstName());
        }
        if (bookUpdates.getCategory() != null) {
            existingBook.setCategory(bookUpdates.getCategory());
        }
        if (bookUpdates.getStatus() != null) {
            existingBook.setStatus(bookUpdates.getStatus());
        }
        if (bookUpdates.getFrontCoverImg() != null) {
            existingBook.setFrontCoverImg(bookUpdates.getFrontCoverImg());
        }
        if (bookUpdates.getCopyNumber() >= 0) {
            existingBook.setCopyNumber(bookUpdates.getCopyNumber());
        }
    }
}
