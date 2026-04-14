package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.dto.BookDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAll(@NonNull Pageable pageable);

    boolean existsBooksByIsbn(String isbn);
    Optional<Book> findBooksByTitle(String title);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(b.firstName, ' ', b.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(b.lastName, ' ', b.firstName)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.isbn) = LOWER(:query)")
    Page<Book> searchBooksByQuery(@Param("query") String query, @NonNull Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "(:category IS NULL OR LOWER(b.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:status IS NULL OR LOWER(b.status) = LOWER(:status))")
    Page<Book> getBooksByFilters(@Param("category") String category, @Param("status") String status, @NonNull Pageable pageable);
}
