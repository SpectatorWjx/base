package com.wang.base.model;

import com.wang.base.common.TableIdPrefix;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Data
@Entity
@Table(name = "user")
@TableIdPrefix("TBUSER")
@SQLDelete(sql = "update user set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class UserEntity extends BaseEntity{

    private String username;

    private String password;

    private String phone;


    private Boolean locked = false;
}