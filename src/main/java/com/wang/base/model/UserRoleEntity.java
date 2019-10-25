package com.wang.base.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Data
@Entity
@Table(name = "user_role")
public class UserRoleEntity extends BaseEntity{

    private Integer userId;

    private Integer roleId;
}