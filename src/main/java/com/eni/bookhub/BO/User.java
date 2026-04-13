package com.eni.bookhub.BO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Builder

@ToString(of = {"id", "email", "phoneNumber", "firstName", "lastName", "userRole", "comments", "loans", "reservations"})
@Table(name = "[USER]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotBlank
    @NotNull
    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @NotBlank
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 250)
    private String password;

    @NotBlank
    @NotNull
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @NotBlank
    @NotNull
    @Column(name = "first_name", nullable = false, length = 110)
    private String firstName;

    @NotBlank
    @NotNull
    @Column(name = "last_name", nullable = false, length = 110)
    private String lastName;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="role_id")
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}