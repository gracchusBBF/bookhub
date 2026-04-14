package com.eni.bookhub.service;

import com.eni.bookhub.dto.ReservationDTO;

public interface ReservationService {
    boolean reserveBook(ReservationDTO reservationDTO);

}
