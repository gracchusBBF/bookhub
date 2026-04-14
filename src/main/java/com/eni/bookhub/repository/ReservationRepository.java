package com.eni.bookhub.repository;

import com.eni.bookhub.BO.Loan;
import com.eni.bookhub.BO.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
