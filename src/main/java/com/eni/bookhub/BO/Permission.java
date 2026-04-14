package com.eni.bookhub.BO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "PERMISSION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;

    @Builder.Default
    @ManyToMany(mappedBy = "listPermission")
    private List<UserRole> userRole = new ArrayList<>();
}