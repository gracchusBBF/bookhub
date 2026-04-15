package com.eni.bookhub.controller;

import com.eni.bookhub.dto.LoanDTO;
import com.eni.bookhub.dto.UserDTO;
import com.eni.bookhub.service.LoanServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @PostMapping("/api/loans")
    public ResponseEntity<Void> save(@RequestBody LoanDTO loan) {
        if (loanServiceImpl.createLoan(loan)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/loans/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int id) {
        if (loanServiceImpl.updateLoan(id)){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/api/loans")
    public List<LoanDTO> listLoans() {
        return loanServiceImpl.listLoans();
    }

    @GetMapping("/api/loans/active")
    public ResponseEntity<?> listActiveLoans() {
        try {
            List<LoanDTO> activeLoansList = loanServiceImpl.listActiveLoans();
            if (activeLoansList.isEmpty() || activeLoansList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(activeLoansList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/api/loans/overdue")
    public ResponseEntity<?> listOverdueLoans() {
        try {
            List<LoanDTO> overdueLoansList = loanServiceImpl.listOverdueLoans();
            if (overdueLoansList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(overdueLoansList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/api/loans/{userEmail}")
    public List<LoanDTO> listLoans(@PathVariable String userEmail) {
        return loanServiceImpl.listLoanByUserEmail(userEmail);
    }

    @GetMapping(value = "/api/loans/stats/totalLoans")
    public ResponseEntity<String> totalLoans() {
        try {
            String numberTotalLoans = loanServiceImpl.numberTotalLoans().toString();
            if (numberTotalLoans == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(numberTotalLoans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/api/loans/stats/activeLoans")
    public ResponseEntity<String> activeLoans() {
        try {
            String numberActiveLoans = loanServiceImpl.numberActiveLoans().toString();
            if (numberActiveLoans == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(numberActiveLoans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}