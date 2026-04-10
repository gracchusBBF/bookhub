package com.eni.bookhub.BO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Table(name="BOOK")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id", nullable=false)
    private Integer id;

    @NotBlank
    @NotNull
    @Size(max = 110)
    @Column(name="title", nullable=false)
    private String title;

    @NotBlank
    @NotNull
    @Size(max = 60)
    @Column(name="last_name", nullable=false)
    private String lastName;

    @NotBlank
    @NotNull
    @Size(max = 60)
    @Column(name="first_name", nullable=false)
    private String firstName;

    @NotBlank
    @NotNull
    @Size(max = 30)
    @Column(name="isbn", nullable=false, unique=true)
    private String isbn;

    @NotBlank
    @NotNull
    @Size(max = 240)
    @Column(name="category", nullable=false)
    private String category;

    @NotBlank
    @NotNull
    @Size(max = 120)
    @Builder.Default
//    @Enumerated(EnumType.STRING)
    private String status = "AVAILABLE";

    @NotBlank
    @NotNull
    @Size(max = 240)
    @Column(name="front_cover_img", nullable=false)
    private String frontCoverImg;

    @NotNull
    @Column(name="copy_number")
    @Builder.Default
    private int copyNumber = 1;

    @OneToMany(mappedBy="book", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "book")
    private Loan loans;

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations;

}
