package com.wang.base.config;

import com.wang.base.common.exception.BaseException;
import com.wang.base.common.utils.RedisUtil;
import com.wang.base.dao.PermissionJpa;
import com.wang.base.dao.UserJpa;
import com.wang.base.dao.UserRoleJpa;
import com.wang.base.common.enums.ResultEnum;
import com.wang.base.model.PermissionEntity;
import com.wang.base.model.User;
import com.wang.base.model.UserEntity;
import com.wang.base.model.UserRoleEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Service("userDetailsService")
@Transactional
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private PermissionJpa permissionJpa;
    @Autowired
    private UserRoleJpa userRoleJpa;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String key = "user_" + username;
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey && redisUtil.getExpire(key)>0) {
            Integer count = Integer.parseInt(redisUtil.get(key));
            if(count >= 5) {
                throw new BaseException(ResultEnum.ACCOUNT_IS_LOCKED.getCode(),ResultEnum.ACCOUNT_IS_LOCKED.getMessage());
            }
        }
        UserEntity userEntity = userJpa.findByPhone(username);
        if (userEntity == null){
            throw new UsernameNotFoundException(username);
        }

        User user = new User();
        BeanUtils.copyProperties(userEntity,user);
        user.setUserId(userEntity.getId());

        List<UserRoleEntity> userRoles = userRoleJpa.findAllByUserId(userEntity.getId());
        if(!userRoles.isEmpty()) {
            List<String> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
            List<PermissionEntity> permissions = permissionJpa.selectByRoleIds(roleIds);
            if (!CollectionUtils.isEmpty(permissions)) {
                Set<SimpleGrantedAuthority> sga = new HashSet<>();
                permissions.forEach(p -> {
                    sga.add(new SimpleGrantedAuthority(p.getPermissionCode()));
                });
                user.setAuthorities(sga);
            }
        }
        return user;
    }
}
