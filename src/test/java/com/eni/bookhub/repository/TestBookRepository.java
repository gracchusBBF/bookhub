//package com.eni.bookhub.repository;
//
//import com.eni.bookhub.BO.Book;
//import com.eni.bookhub.dto.BookDTO;
//import jakarta.persistence.EntityManager;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Optional;
//
//@Slf4j
//@DataJpaTest
//@ActiveProfiles("test")
//public class TestBookRepository {
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    void test_save(){
//
//        BookDTO newBook = Book.builder()
//                .title("Le petit prince")
//                .lastName("Test")
//                .firstName("Test")
//                .isbn("010-2070612759")
//                .category("Drame")
//                .status("AVAILABLE")
//                .frontCoverImg("parici.fr")
//                .copyNumber(1)
//                .build();
//
//        bookRepository.save(newBook);
//        bookRepository.flush();
//        entityManager.clear();
//
//        Optional<BookDTO> optionalBook =
//                bookRepository.findBooksByTitle("Le petit prince");
//
//        Assertions.assertThat(optionalBook).isPresent();
//        log.info(optionalBook.get().toString());
//    }
//
//    @Test
//    void test_delete(){
//        // Arrange
//        BookDTO newBook = Book.builder()
//                .title("Le petit prince")
//                .lastName("Test")
//                .firstName("Test")
//                .isbn("010-2070612759")
//                .category("Drame")
//                .status("AVAILABLE")
//                .frontCoverImg("parici.fr")
//                .copyNumber(1)
//                .build();
//
//        BookDTO savedBook = bookRepository.save(newBook);
//        bookRepository.flush();
//        entityManager.clear();
//
//        // Act
//        bookRepository.deleteById(savedBook.getId());
//        bookRepository.flush();
//        entityManager.clear();
//
//        // Assert
//        Optional<BookDTO> optionalBook = bookRepository.findById(savedBook.getId());
//        Assertions.assertThat(optionalBook).isEmpty();
//        log.info("Livre supprimé avec l'ID : {}", savedBook.getId());
//    }
//
//    @Test
//    void test_update(){
//        // Arrange
//        BookDTO newBook = Book.builder()
//                .title("Le petit prince")
//                .lastName("Test")
//                .firstName("Test")
//                .isbn("010-2070612759")
//                .category("Drame")
//                .status("AVAILABLE")
//                .frontCoverImg("parici.fr")
//                .copyNumber(1)
//                .build();
//
//        BookDTO savedBook = bookRepository.save(newBook);
//        bookRepository.flush();
//        entityManager.clear();
//
//        // Act
//        BookDTO bookToUpdate = bookRepository.findById(savedBook.getId()).orElseThrow();
//        bookToUpdate.setTitle("Le grand prince");
//        bookToUpdate.setStatus("UNAVAILABLE");
//        bookRepository.save(bookToUpdate);
//        bookRepository.flush();
//        entityManager.clear();
//
//        // Assert
//        BookDTO updatedBook = bookRepository.findById(savedBook.getId()).orElseThrow();
//        Assertions.assertThat(updatedBook.getTitle()).isEqualTo("Le grand prince");
//        Assertions.assertThat(updatedBook.getStatus()).isEqualTo("UNAVAILABLE");
//        log.info(updatedBook.toString());
//    }
//}
