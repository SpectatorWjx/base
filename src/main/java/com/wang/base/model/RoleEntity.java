package com.wang.base.model;

import com.wang.base.common.TableIdPrefix;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
@TableIdPrefix("TBROLE")
@SQLDelete(sql = "update role set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class RoleEntity extends BaseEntity{

    private String roleName;
}