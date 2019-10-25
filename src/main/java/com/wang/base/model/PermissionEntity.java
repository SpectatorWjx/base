package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "permission")
public class PermissionEntity extends BaseEntity{

    private String permissionName;

    private String permissionCode;

    private String url;
}