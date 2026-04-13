package com.eni.bookhub.controller;

import com.eni.bookhub.dto.LoanDTO;
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
    @GetMapping(value = "/api/loans/{userId}")
    public List<LoanDTO> listLoans(@PathVariable int userId) {
        return loanServiceImpl.listLoanByUserId(userId);
    }
}