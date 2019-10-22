package com.wang.base.config;

import com.wang.base.dao.PermissionJpa;
import com.wang.base.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    PermissionJpa permissionJpa;

    private FilterInvocationSecurityMetadataSource superMetadataSource;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String,String> urlRoleMap = new HashMap();

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public MySecurityMetadataSource(FilterInvocationSecurityMetadataSource expressionBasedFilterInvocationSecurityMetadataSource){
        this.superMetadataSource = expressionBasedFilterInvocationSecurityMetadataSource;

    }



    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if(urlRoleMap.size() == 0) {
            loadResourcePermission();
        }
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            if(antPathMatcher.match(entry.getKey(),url)){
                return SecurityConfig.createList(entry.getValue());
            }
        }
        return superMetadataSource.getAttributes(object);
    }

    private void loadResourcePermission() {
        List<Permission> permissions = permissionJpa.findAll();
        permissions.stream().forEach(p -> urlRoleMap.put(p.getUrl(),p.getPermissionCode()));
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

//
//    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    private volatile Map<String,String> urlPermMap = null;
//
//    @PostConstruct
//    public void init() {
//        loadResourcePermission();
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//
//
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        if (null == urlPermMap) {
//            synchronized (LoginSecurityMetadataSource.class) {
//                if (null == urlPermMap) {
//                    loadResourcePermission();
//                }
//            }
//        }
//        FilterInvocation fi = (FilterInvocation) object;
//        String url = fi.getRequestUrl();
//
//        for(Map.Entry<String,String> entry:urlPermMap.entrySet()){
//            if(antPathMatcher.match(entry.getKey(),url)){
//                return SecurityConfig.createList(entry.getValue());
//            }
//        }
//        return null;
//    }
//
//    private void loadResourcePermission() {
//        urlPermMap = new HashMap<>();
//        List<AuthoritiesEntity> permissions = authoritiesRepository.findAll();
//        permissions.stream().forEach(p -> urlPermMap.put(p.getUrl(),p.getAuthority()));
//    }
//
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return FilterInvocation.class.isAssignableFrom(clazz);
//    }
}