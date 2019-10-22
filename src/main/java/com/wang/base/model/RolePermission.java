package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class RolePermission extends BaseEntity{

    private Integer roleId;

    private Integer permissionId;
}