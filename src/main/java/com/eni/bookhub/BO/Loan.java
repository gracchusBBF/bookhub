package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "LOAN")
@Data
public class Loan {

    @Id
    @Column(name = "loan_id")
    private int id;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private Date deadline;
}