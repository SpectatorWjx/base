package com.wang.base.service;

import com.wang.base.model.Permission;

import java.util.List;

/***
 * @ClassName: PermissionService
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/9 10:02
 */
public interface PermissionService {
    List<Permission> findAllPermission();
}
