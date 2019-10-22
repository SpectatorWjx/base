package com.wang.base.dao;

import com.wang.base.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @ClassName: UserJpa
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/8 10:59
 */
@Repository
public interface PermissionJpa extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor {

    @Query(value ="select p from Permission p  where p.id in (select rp.permissionId from RolePermission rp where rp.roleId in ?1)")
    List<Permission> selectByRoleIds(List<Integer> roleIds);
}
