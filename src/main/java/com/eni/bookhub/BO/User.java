package com.eni.bookhub.BO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Data
@Table(name = "[USER]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}