package com.wang.base.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Set;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Data
@Entity
public class User extends BaseEntity implements UserDetails {

    private String username;

    private String password;

    private String phone;

    private Boolean locked;

    @Override
    public String getUsername() {
        return getPhone();
    }

    @Getter
    @Setter
    @Transient
    private Set<SimpleGrantedAuthority> permissions;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions;
    }

    public void setAuthorities(Set<SimpleGrantedAuthority> permissions){
        this.permissions = permissions;
    }
}