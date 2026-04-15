package com.eni.bookhub.controller;

import com.eni.bookhub.BO.User;
import com.eni.bookhub.dto.LoanDTO;
import com.eni.bookhub.dto.ReservationDTO;
import com.eni.bookhub.service.LoanServiceImpl;
import com.eni.bookhub.service.ReservationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ReservationController {

    private final ReservationServiceImpl reservationServiceImpl;

    public ReservationController(ReservationServiceImpl reservationServiceImpl) {
        this.reservationServiceImpl = reservationServiceImpl;
    }

    @PostMapping("/api/reservations")
    public ResponseEntity<Void> saveReservation(@RequestBody ReservationDTO reservation){
        if (reservationServiceImpl.reserveBook(reservation)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
    }

}
