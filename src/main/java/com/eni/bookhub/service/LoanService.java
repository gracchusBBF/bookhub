package com.eni.bookhub.service;



import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.dto.LoanDTO;

import java.util.List;

public interface LoanService {

    List<LoanDTO> listLoans();

    List<LoanDTO> listLoanByUserEmail(String userEmail);

    List<LoanDTO> listActiveLoans();

    List<LoanDTO> listOverdueLoans();

    Integer numberTotalLoans();
    Integer numberActiveLoans();

    Boolean getLoanById(int id);

    Boolean createLoan(LoanDTO loan);

    Boolean updateLoan(int id);
}