package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
