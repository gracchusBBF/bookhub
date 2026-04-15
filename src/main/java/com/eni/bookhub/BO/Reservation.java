package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="RESERVATION")
@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(60) CHECK (status IN ('ACTIVE', 'PENDING', 'INACTIVE'))")
    private String status = "INACTIVE";

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

}
