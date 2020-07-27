package com.puhui.email.config;

import com.puhui.email.entity.Menu;
import com.puhui.email.entity.Role;
import com.puhui.email.mapper.MenuMapper;
import com.puhui.email.service.RoleService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/21
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation)object).getRequestUrl();
        List<Menu> allMenu=menuMapper.getAllMenu();
        for (Menu menu:allMenu) {
            if (antPathMatcher.match(menu.getPattern(),requestUrl)){
                List<Role> roles=menu.getRoles();
                String[] roleArr=new String[roles.size()];
                for (int i=0;i<roleArr.length;i++){
                    roleArr[i]=roles.get(i).getRole_name();
                }
                return SecurityConfig.createList(roleArr);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
