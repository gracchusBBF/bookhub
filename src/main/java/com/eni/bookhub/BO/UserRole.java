package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
//@ToString(of = {"id", "roleName"})
@Builder

@Entity
@Table(name = "ROLE")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name= "role_name", nullable = false, unique = true, length = 120)
    private String roleName;

    @OneToMany(mappedBy = "userRole")
    @ToString.Exclude
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "PERMISSION_ROLE",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> listPermission = new ArrayList<>();
}