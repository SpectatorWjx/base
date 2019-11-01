package com.wang.base.dao;

import com.wang.base.model.PermissionEntity;
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
public interface PermissionJpa extends JpaRepository<PermissionEntity, String>, JpaSpecificationExecutor {

    @Query(value ="select p from PermissionEntity p  where p.id in (select rp.permissionId from RolePermissionEntity rp where rp.roleId in ?1)")
    List<PermissionEntity> selectByRoleIds(List<String> roleIds);
}
