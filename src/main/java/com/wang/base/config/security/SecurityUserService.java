package com.wang.base.config.security;

import com.wang.base.common.exception.BaseException;
import com.wang.base.config.redis.RedisUtil;
import com.wang.base.dao.PermissionJpa;
import com.wang.base.dao.UserJpa;
import com.wang.base.dao.UserRoleJpa;
import com.wang.base.model.Permission;
import com.wang.base.model.User;
import com.wang.base.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
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
                throw new BaseException("账号已锁定");
            }
        }
        User user = userJpa.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }

        List<UserRole> userRoles = userRoleJpa.findAllByUserId(user.getId());
        List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        List<Permission> permissions = permissionJpa.selectByRoleIds(roleIds);
        if (!CollectionUtils.isEmpty(permissions)){
            Set<SimpleGrantedAuthority> sga = new HashSet<>();
            permissions.forEach(p->{
                sga.add(new SimpleGrantedAuthority(p.getPermissionCode()));
            });
            user.setAuthorities(sga);
        }
        return user;
    }
}
