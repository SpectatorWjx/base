package com.wang.base.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity{

    private String username;

    private String password;

    private String phone;


    private Boolean locked = false;
}