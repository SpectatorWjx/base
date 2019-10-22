package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class UserRole extends BaseEntity{

    private Integer userId;

    private Integer roleId;
}