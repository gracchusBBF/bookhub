package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.repository.LoanRepository;
import org.springframework.stereotype.Service;
import com.eni.bookhub.repository.BookRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }


    public List<Loan> listLoanByUserId(int userId){
        return loanRepository.findByUserId(userId);
    }
    public Boolean getLoanById(int id){
        try{
        loanRepository.findById(id).orElse(null);
        return true;}
        catch (Exception e){
            return false;
        }
    }
    public Boolean createLoan(Loan loan) {
        try {
            Book book = loan.getBook();
            User user = loan.getUser();
            boolean isUserAllowedToBorrow = isUserAllowed(user);
            if (!isUserAllowedToBorrow) {
                System.err.println("Refus: l'utilisateur" + user.getLastName()+ "a un ou des livres en retard");
                throw new RuntimeException("Utilisateur bloqué");
            }

            if (isUserFull(user)) {
                System.err.println("Refus : L'utilisateur " + user.getId() + " a déjà 3 prêts.");
                throw new RuntimeException("Limite de 3 emprunts atteinte.");
            }
            if (book.getCopyNumber()<1) {
                System.err.println("Refus : Livre ID " + book.getId() + " indisponible.");
                throw new RuntimeException("Le livre n'est pas disponible.");
            }



            loanRepository.save(loan);
            book.setCopyNumber(book.getCopyNumber()-1);
            bookRepository.save(book);
            return true;

        } catch (RuntimeException e) {
            // Ici on intercepte tes messages personnalisés
            System.out.println("Erreur métier : " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Ici on intercepte les vraies erreurs techniques (SQL, NullPointer, etc.)
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
    public Boolean updateLoan(int id){
        try {
            Loan loan = loanRepository.findById(id).orElse(null);
            Book book = loan.getBook();
            long now = System.currentTimeMillis();
            loan.setReturnDate(new java.sql.Date(now));
            book.setCopyNumber(book.getCopyNumber()+1);
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
            if (loan.getReturnDate() != null) {
                return true;
            }
            if (loan.getStartDate() != null) {
                LocalDate startDate = loan.getStartDate().toLocalDateTime().toLocalDate();
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, today);
                if (daysBetween > 14) {
                    return false;
                }
            }
        }
        return true;
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