package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role_permission")
public class RolePermissionEntity extends BaseEntity{

    private Integer roleId;

    private Integer permissionId;
}