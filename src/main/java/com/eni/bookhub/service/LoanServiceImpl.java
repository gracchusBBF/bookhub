package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.LoanDTO; // Import de votre nouveau DTO
import com.eni.bookhub.repository.BookRepository;
import com.eni.bookhub.repository.LoanRepository;
import com.eni.bookhub.repository.UserRepository; // Nécessaire pour charger le User
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Convertit une Entité en DTO
    private LoanDTO convertToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setStartDate(loan.getStartDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setDeadline(loan.getDeadline());
        if (loan.getUser() != null) dto.setUserId(loan.getUser().getId());
        if (loan.getBook() != null) {
            dto.setBookId(loan.getBook().getId());
            dto.setBookTitle(loan.getBook().getTitle());
        }
        return dto;
    }
    public List<LoanDTO> listLoans() {
        return loanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> listLoanByUserId(int userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> listActiveLoans() {
        return loanRepository.findActiveLoans().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> listOverdueLoans() {
        return loanRepository.findOverdueLoans().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Boolean getLoanById(int id) {
        return loanRepository.findById(id).isPresent();
    }
    public Boolean createLoan(LoanDTO loanDTO) {
        try {
            // 1. Recharger les entités complètes depuis la BDD via les IDs du DTO
            Book book = bookRepository.findById(loanDTO.getBookId())
                    .orElseThrow(() -> new RuntimeException("Livre introuvable."));

            User user = userRepository.findById(loanDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

            // 2. Créer l'entité à partir du DTO
            Loan loan = new Loan();
            loan.setStartDate(loanDTO.getStartDate());
            loan.setDeadline(loanDTO.getDeadline());
            loan.setBook(book);
            loan.setUser(user);

            // 3. Vérifications métier
            if (!isUserAllowed(user)) {
                throw new RuntimeException("Utilisateur bloqué : prêt(s) en retard.");
            }
            if (isUserFull(user)) {
                throw new RuntimeException("Limite de 3 emprunts atteinte.");
            }
            if (book.getCopyNumber() < 1) {
                throw new RuntimeException("Le livre n'est pas disponible.");
            }

            // 4. Sauvegarde
            book.setCopyNumber(book.getCopyNumber() - 1);
            bookRepository.save(book);
            loanRepository.save(loan);

            return true;
        } catch (Exception e) {
            System.err.println("Erreur creation prêt : " + e.getMessage());
            return false;
        }
    }

    public Boolean updateLoan(int id) {
        try {
            Loan loan = loanRepository.findById(id).orElse(null);
            if (loan == null) return false;

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

    // Garde les méthodes privées avec les Entités pour la logique interne
    private boolean isUserAllowed(User user) {
        List<Loan> userLoans = loanRepository.findByUserId(user.getId());
        LocalDate today = LocalDate.now();

        for (Loan loan : userLoans) {
            if (loan.getReturnDate() == null && loan.getStartDate() != null) {
                LocalDate startDate = loan.getStartDate().toLocalDateTime().toLocalDate();
                if (ChronoUnit.DAYS.between(startDate, today) > 14) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isUserFull(User user) {
        return loanRepository.findByUserId(user.getId()).stream()
                .filter(l -> l.getReturnDate() == null)
                .count() >= 3;
    }
}