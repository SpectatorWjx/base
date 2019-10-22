package com.wang.base.dao;

import com.wang.base.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/***
 * @ClassName: UserJpa
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/8 10:59
 */
@Repository
public interface RoleJpa extends JpaRepository<Role, Integer>, JpaSpecificationExecutor {

}
