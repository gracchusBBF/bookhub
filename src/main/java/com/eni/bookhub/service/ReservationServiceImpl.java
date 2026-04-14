package com.eni.bookhub.service;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.BO.Reservation;
import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.ReservationDTO;
import com.eni.bookhub.repository.BookRepository;
import com.eni.bookhub.repository.ReservationRepository;
import com.eni.bookhub.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    public ReservationServiceImpl(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean reserveBook(ReservationDTO reservationDTO) {
        // 1. Récupérer les entités complètes depuis la BDD (nécessite les repositories correspondants)
      /*  Book book = bookRepository.findById(reservationDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable."));

        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
*/
      /*  // 2. Créer l'objet métier (Entité)
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);*/

      /*  // 3. Sauvegarder l'entité
        reservationRepository.save(reservation);*/
        return false;
    }
}
