package com.wang.base.service.impl;

import com.wang.base.model.Permission;
import com.wang.base.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/***
 * @ClassName: PermissionServiceImpl
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/9 10:03
 */
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionService permissionJpa;
    @Override
    public List<Permission> findAllPermission() {
        return permissionJpa.findAllPermission();
    }
}
