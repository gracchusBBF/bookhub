package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.repository.LoanRepository;
import com.eni.bookhub.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/API/loans")
    public ResponseEntity<Void> save(@RequestBody Loan loan) {
        if (loanService.createLoan(loan)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/API/loans/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int id) {
        if (loanService.updateLoan(id)){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/API/loans")
    public List<Loan> listLoans() {
        return loanService.listLoans();
    }
    @GetMapping(value = "/API/loans/{userId}")
    public List<Loan> listLoans(@PathVariable int userId) {
        return loanService.listLoanByUserId(userId);
    }
}