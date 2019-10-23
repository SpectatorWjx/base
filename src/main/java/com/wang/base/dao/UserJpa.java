package com.wang.base.dao;

import com.wang.base.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @ClassName: UserJpa
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/8 10:59
 */
@Repository
public interface UserJpa extends JpaRepository<User, Integer>, JpaSpecificationExecutor {
    User findByUsername(String username);

    List<User> findByUsernameIn(List<String> username);
}
