package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsBooksByIsbn(String isbn);

    Optional<Book> findBooksByTitle(String title);
}
