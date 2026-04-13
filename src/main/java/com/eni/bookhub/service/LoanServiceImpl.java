package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.repository.BookRepository;
import com.eni.bookhub.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }


    public List<Loan> listLoanByUserId(int userId){
        return loanRepository.findByUserId(userId);
    }
    public Boolean getLoanById(int id) {
        return loanRepository.findById(id).isPresent(); // ✅ simple et correct
    }
    public Boolean createLoan(Loan loan) {
        try {
            // ✅ Recharger le Book complet depuis la BDD
            Book book = bookRepository.findById(loan.getBook().getId()).orElse(null);
            if (book == null) {
                throw new RuntimeException("Livre introuvable.");
            }

            // ✅ Recharger le User complet depuis la BDD
            User user = loan.getUser();

            // Réassigner les objets complets au loan
            loan.setBook(book);
            loan.setUser(user);

            boolean isUserAllowedToBorrow = isUserAllowed(user);
            if (!isUserAllowedToBorrow) {
                throw new RuntimeException("Utilisateur bloqué : prêt(s) en retard.");
            }

            if (isUserFull(user)) {
                throw new RuntimeException("Limite de 3 emprunts atteinte.");
            }

            if (book.getCopyNumber() < 1) {
                throw new RuntimeException("Le livre n'est pas disponible.");
            }

            loanRepository.save(loan);
            book.setCopyNumber(book.getCopyNumber() - 1);
            bookRepository.save(book);
            return true;

        } catch (RuntimeException e) {
            System.out.println("Erreur métier : " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Loan> listLoans(){
        try{
            return loanRepository.findAll();
        } catch (Exception e){
            return null;
        }
    }
    public Boolean updateLoan(int id) {
        try {
            Loan loan = loanRepository.findById(id).orElse(null);
            if (loan == null) return false; // ✅ évite NullPointerException

            Book book = loan.getBook();
            loan.setReturnDate(new java.sql.Date(System.currentTimeMillis()));
            book.setCopyNumber(book.getCopyNumber() + 1);
            bookRepository.save(book);
            loanRepository.save(loan);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isUserAllowed(User user) {
        List<Loan> userLoans = listLoanByUserId(user.getId());
        LocalDate today = LocalDate.now();

        for (Loan loan : userLoans) {
            // On ignore les prêts déjà rendus
            if (loan.getReturnDate() != null) {
                continue; // ✅ on skip, pas return true
            }
            // Prêt actif : on vérifie le délai
            if (loan.getStartDate() != null) {
                LocalDate startDate = loan.getStartDate().toLocalDateTime().toLocalDate();
                long daysBetween = ChronoUnit.DAYS.between(startDate, today);
                if (daysBetween > 14) {
                    return false; // ❌ prêt actif en retard → utilisateur bloqué
                }
            }
        }
        return true; // ✅ aucun prêt actif en retard
    }
    private boolean isUserFull(User user) {
        List<Loan> userLoans = listLoanByUserId(user.getId());
        int activeLoans = 0;

        for (Loan loan : userLoans) {
            if (loan.getReturnDate() == null) {
                activeLoans++;
            }
        }
        return activeLoans >= 3;
    }
}