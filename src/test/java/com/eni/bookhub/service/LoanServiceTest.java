package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
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

    @InjectMocks
    private LoanService loanService;

    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setLastName("Dupont");

        book = new Book();
        book.setId(1);
        book.setCopyNumber(3);

        loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
    }

    // ===================== createLoan =====================

    @Test
    void createLoan_success() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of());

        Boolean result = loanService.createLoan(loan);

        assertTrue(result);
        verify(loanRepository).save(loan);
        verify(bookRepository).save(book);
        assertEquals(2, book.getCopyNumber());
    }

    @Test
    void createLoan_bookNotFound_returnsFalse() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Boolean result = loanService.createLoan(loan);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userNotFound_returnsFalse() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Boolean result = loanService.createLoan(loan);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_bookUnavailable_returnsFalse() {
        book.setCopyNumber(0);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of());

        Boolean result = loanService.createLoan(loan);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userHas3ActiveLoans_returnsFalse() {
        Loan l1 = loanWithNoReturnDate();
        Loan l2 = loanWithNoReturnDate();
        Loan l3 = loanWithNoReturnDate();

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of(l1, l2, l3));

        Boolean result = loanService.createLoan(loan);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userHasLateActiveLoan_returnsFalse() {
        Loan lateLoan = new Loan();
        lateLoan.setReturnDate(null);
        // startDate = il y a 20 jours → en retard
        LocalDate lateDate = LocalDate.now().minusDays(20);
        lateLoan.setStartDate(Timestamp.valueOf(lateDate.atStartOfDay()));

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of(lateLoan));

        Boolean result = loanService.createLoan(loan);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_userHasReturnedLoanOnly_success() {
        Loan returnedLoan = new Loan();
        returnedLoan.setReturnDate(new Date(System.currentTimeMillis()));

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of(returnedLoan));

        Boolean result = loanService.createLoan(loan);

        assertTrue(result);
    }

    @Test
    void createLoan_userHas2ActiveLoansNotLate_success() {
        Loan l1 = loanWithNoReturnDate();
        Loan l2 = loanWithNoReturnDate();

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.findByUserId(1)).thenReturn(List.of(l1, l2));

        Boolean result = loanService.createLoan(loan);

        assertTrue(result);
    }

    // ===================== updateLoan =====================

    @Test
    void updateLoan_success() {
        when(loanRepository.findById(1)).thenReturn(Optional.of(loan));
        when(bookRepository.save(any())).thenReturn(book);

        Boolean result = loanService.updateLoan(1);

        assertTrue(result);
        assertNotNull(loan.getReturnDate());
        assertEquals(4, book.getCopyNumber());
        verify(loanRepository).save(loan);
        verify(bookRepository).save(book);
    }

    @Test
    void updateLoan_loanNotFound_returnsFalse() {
        when(loanRepository.findById(99)).thenReturn(Optional.empty());

        Boolean result = loanService.updateLoan(99);

        assertFalse(result);
        verify(loanRepository, never()).save(any());
    }

    // ===================== getLoanById =====================

    @Test
    void getLoanById_exists_returnsTrue() {
        when(loanRepository.findById(1)).thenReturn(Optional.of(loan));

        Boolean result = loanService.getLoanById(1);

        assertTrue(result);
    }

    @Test
    void getLoanById_notExists_returnsFalse() {
        when(loanRepository.findById(99)).thenReturn(Optional.empty());

        Boolean result = loanService.getLoanById(99);

        assertFalse(result);
    }

    // ===================== listLoans =====================

    @Test
    void listLoans_returnsAll() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<Loan> result = loanService.listLoans();

        assertEquals(1, result.size());
    }

    @Test
    void listLoans_repositoryThrows_returnsNull() {
        when(loanRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        List<Loan> result = loanService.listLoans();

        assertNull(result);
    }

    // ===================== listLoanByUserId =====================

    @Test
    void listLoanByUserId_returnsLoans() {
        when(loanRepository.findByUserId(1)).thenReturn(List.of(loan));

        List<Loan> result = loanService.listLoanByUserId(1);

        assertEquals(1, result.size());
    }

    @Test
    void listLoanByUserId_noLoans_returnsEmpty() {
        when(loanRepository.findByUserId(99)).thenReturn(List.of());

        List<Loan> result = loanService.listLoanByUserId(99);

        assertTrue(result.isEmpty());
    }

    // ===================== helpers =====================

    private Loan loanWithNoReturnDate() {
        Loan l = new Loan();
        l.setReturnDate(null);
        l.setStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return l;
    }
}