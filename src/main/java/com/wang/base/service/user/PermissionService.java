package com.wang.base.service.user;

import com.wang.base.model.PermissionEntity;

import java.util.List;

/***
 * @ClassName: PermissionService
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/9 10:02
 */
public interface PermissionService {
    List<PermissionEntity> findAllPermission();
}
