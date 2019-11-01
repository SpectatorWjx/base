package com.wang.base.model;

import com.wang.base.common.TableIdPrefix;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "permission")
@TableIdPrefix("TPERMS")
@SQLDelete(sql = "update permission set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class PermissionEntity extends BaseEntity{

    private String permissionName;

    private String permissionCode;

    private String url;
}