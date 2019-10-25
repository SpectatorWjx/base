package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_role")
public class UserRoleEntity extends BaseEntity{

    private Integer userId;

    private Integer roleId;
}