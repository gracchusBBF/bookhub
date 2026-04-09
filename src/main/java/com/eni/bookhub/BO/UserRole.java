package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@Data
public class UserRole {
    @Id
    @Column(name = "role_id")
    private int id;

    @Column(name= "role_name", nullable = false, unique = true, length = 120)
    private String roleName;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "permission_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    Set<Permission> permissionRole;
}