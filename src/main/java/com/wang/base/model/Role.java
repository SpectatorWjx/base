package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Role extends BaseEntity{

    private String roleName;
}