package com.eni.bookhub.BO;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "permission_role")
public class PermissionRole {
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "permission_id")
    private int permissionId;
}