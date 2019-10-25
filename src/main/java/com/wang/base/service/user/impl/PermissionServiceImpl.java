package com.wang.base.service.user.impl;

import com.wang.base.model.PermissionEntity;
import com.wang.base.service.user.PermissionService;
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
    public List<PermissionEntity> findAllPermission() {
        return permissionJpa.findAllPermission();
    }
}
