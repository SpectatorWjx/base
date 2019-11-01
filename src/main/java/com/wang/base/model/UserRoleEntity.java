package com.wang.base.model;

import com.wang.base.common.TableIdPrefix;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@TableIdPrefix("TUSROL")
@SQLDelete(sql = "update user_role set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class UserRoleEntity extends BaseEntity{

    private String userId;

    private String roleId;
}