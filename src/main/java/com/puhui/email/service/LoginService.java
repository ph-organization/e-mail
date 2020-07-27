package com.puhui.email.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/11
 */
public interface LoginService extends UserDetailsService {
    /**
     * 加载登录用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
