package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.LoanDTO;
import com.eni.bookhub.repository.BookRepository;
import com.eni.bookhub.repository.LoanRepository;
import com.eni.bookhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    // Attention : Assurez-vous que l'interface LoanService est bien implémentée par LoanServiceImpl
    @InjectMocks
    private LoanServiceImpl loanService;

    private User user;
    private Book book;
    private Loan loan;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setLastName("Dupont");

        book = new Book();
        book.setId(1);
        book.setTitle("Le Petit Prince");
        book.setCopyNumber(3);

        loan = new Loan();
        loan.setId(10);
        loan.setUser(user);
        loan.setBook(book);
        loan.setStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));

        // Initialisation du DTO pour les tests de création
        loanDTO = new LoanDTO();
        loanDTO.setUserId(1);
        loanDTO.setBookId(1);
        loanDTO.setStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
    }

    // ===================== createLoan =====================

    @Test
    void createLoan_success() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of());

        Boolean result = loanService.createLoan(loanDTO);

        assertTrue(result);
        verify(loanRepository).save(any(Loan.class));
        verify(bookRepository).save(book);
        assertEquals(2, book.getCopyNumber());
    }

    @Test
    void createLoan_bookNotFound_returnsFalse() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        Boolean result = loanService.createLoan(loanDTO);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userNotFound_returnsFalse() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Boolean result = loanService.createLoan(loanDTO);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userHas3ActiveLoans_returnsFalse() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of(
                loanWithNoReturnDate(), loanWithNoReturnDate(), loanWithNoReturnDate()
        ));

        Boolean result = loanService.createLoan(loanDTO);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    // ===================== listLoans & listLoanByUserId =====================

    @Test
    void listLoans_returnsDtoList() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<LoanDTO> result = loanService.listLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Le Petit Prince", result.get(0).getBookTitle()); // Vérifie le mapping
    }

    @Test
    void listLoanByUserId_returnsDtoList() {
        when(loanRepository.findByUserId(1)).thenReturn(List.of(loan));

        List<LoanDTO> result = loanService.listLoanByUserId(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getUserId());
    }

    // ===================== updateLoan =====================

    @Test
    void updateLoan_success() {
        // L'update utilise toujours l'ID technique
        when(loanRepository.findById(10)).thenReturn(Optional.of(loan));

        Boolean result = loanService.returnLoan(10);

        assertTrue(result);
        assertNotNull(loan.getReturnDate());
        assertEquals(4, book.getCopyNumber());
        verify(loanRepository).save(loan);
    }

    // ===================== helpers =====================

    private Loan loanWithNoReturnDate() {
        Loan l = new Loan();
        l.setReturnDate(null);
        l.setStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return l;
    }
}