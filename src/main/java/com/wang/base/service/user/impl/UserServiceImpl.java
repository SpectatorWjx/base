package com.wang.base.service.user.impl;

import com.wang.base.dao.UserJpa;
import com.wang.base.model.UserEntity;
import com.wang.base.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 * @ClassName: UserServiceImpl
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/25 10:26
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserJpa userJpa;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String phone) {
        UserEntity user = new UserEntity();
        user.setPhone(phone);
        userJpa.saveAndFlush(user);
    }
}
