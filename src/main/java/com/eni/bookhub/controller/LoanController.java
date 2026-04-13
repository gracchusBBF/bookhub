package com.eni.bookhub.controller;

import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.service.LoanServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @PostMapping("/API/loans")
    public ResponseEntity<Void> save(@RequestBody Loan loan) {
        if (loanServiceImpl.createLoan(loan)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/API/loans/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int id) {
        if (loanServiceImpl.updateLoan(id)){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/API/loans")
    public List<Loan> listLoans() {
        return loanServiceImpl.listLoans();
    }
    @GetMapping(value = "/API/loans/{userId}")
    public List<Loan> listLoans(@PathVariable int userId) {
        return loanServiceImpl.listLoanByUserId(userId);
    }
}