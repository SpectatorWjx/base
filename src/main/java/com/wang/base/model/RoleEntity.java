package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity{

    private String roleName;
}