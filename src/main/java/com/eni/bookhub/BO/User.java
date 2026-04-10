package com.eni.bookhub.BO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "[USER]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false, length = 110)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 110)
    private String lastName;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="role_id")
    private UserRole userRole;

   // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
   // @JsonManagedReference("user-loans")
    //private List<Loan> loans;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}