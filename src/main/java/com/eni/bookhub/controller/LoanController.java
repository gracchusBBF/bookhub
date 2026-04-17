package com.eni.bookhub.controller;

import com.eni.bookhub.dto.LoanDTO;
import com.eni.bookhub.service.LoanService; // Utilisation de l'interface
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans") // On centralise le prefixe ici
@Tag(name = "Loans", description = "Gestion des emprunts par les utilisateurs")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody LoanDTO loan) {
        if (loanService.createLoan(loan)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int id) {
        if (loanService.returnLoan(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public List<LoanDTO> listLoans() {
        return loanService.listLoans();
    }

    // Changement du path pour éviter les conflits avec /active ou /overdue
    @GetMapping("/user/{userEmail}")
    public List<LoanDTO> listLoansByUser(@PathVariable String userEmail) {
        return loanService.listLoanByUserEmail(userEmail);
    }

    @GetMapping("/active")
    public ResponseEntity<?> listActiveLoans() {
        try {
            List<LoanDTO> activeLoansList = loanService.listActiveLoans();
            if (activeLoansList == null || activeLoansList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(activeLoansList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> listOverdueLoans() {
        try {
            List<LoanDTO> overdueLoansList = loanService.listOverdueLoans();
            if (overdueLoansList == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(overdueLoansList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stats/totalLoans")
    public ResponseEntity<String> totalLoans() {
        try {
            return ResponseEntity.ok(loanService.numberTotalLoans().toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stats/activeLoans")
    public ResponseEntity<String> activeLoans() {
        try {
            return ResponseEntity.ok(loanService.numberActiveLoans().toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}