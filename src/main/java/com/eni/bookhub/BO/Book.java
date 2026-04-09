package com.eni.bookhub.BO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="BOOK")
public class Book {
    @Id
    @Column(name="book_id", nullable=false)
    private int id;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="isbn", nullable=false, unique=true)
    private String isbn;

    @Column(name="category", nullable=false)
    private String category;

    @Column(name="available", nullable=false)
    private String available;

    @Column(name="front_cover_img", nullable=false)
    private String frontCoverImg;

    @Column(name="copy_number")
    private int copyNumber;

    @OneToMany(mappedBy="book", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations;

}
