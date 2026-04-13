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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Integer id;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "return_date")
    private Date returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "deadline", nullable = false)
    private Date deadline;
}