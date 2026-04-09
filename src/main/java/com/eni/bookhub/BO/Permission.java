package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "permission")
@Data
public class Permission {

    @Id
    @Column(name = "permission_id")
    private int id;

    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;

}