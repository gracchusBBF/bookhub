package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByUserEmail(String email);
    List<Loan> findByUserId(int userId);

    // Active loans
    @Query("SELECT l FROM Loan l WHERE l.returnDate IS NULL")
    List<Loan> findActiveLoans();

    // Overdue
    @Query("SELECT l FROM Loan l WHERE l.returnDate IS NULL AND l.deadline < CURRENT DATE ")
    List<Loan> findOverdueLoans();
}
