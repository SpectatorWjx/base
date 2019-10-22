package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Permission extends BaseEntity{

    private String permissionName;

    private String permissionCode;

    private String url;
}