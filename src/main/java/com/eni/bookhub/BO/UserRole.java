package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "ROLE")
public class UserRole {
    @Id
    @Column(name = "role_id")
    private int id;

    @Column(name= "role_name", nullable = false, unique = true, length = 120)
    private String roleName;

    @OneToMany(mappedBy = "userRole")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "PERMISSION_ROLE_TEST",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> listPermission = new ArrayList<>();
}