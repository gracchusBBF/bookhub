package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="reservation")
@Data
@Entity
public class Reservation {
    @Id
    private int id;
    @Column(name="status", nullable = true)
    private int status;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

}
