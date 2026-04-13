package com.eni.bookhub.service;



import com.eni.bookhub.BO.Loan;
import java.util.List;

public interface LoanService {

    List<Loan> listLoans();

    List<Loan> listLoanByUserId(int userId);

    Boolean getLoanById(int id);

    Boolean createLoan(Loan loan);

    Boolean updateLoan(int id);
}