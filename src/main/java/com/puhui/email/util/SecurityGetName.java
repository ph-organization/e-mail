package com.puhui.email.util;

import com.puhui.email.entity.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @description: 获取登录用户名
 * @author: 杨利华
 * @date: 2020/7/13
 */
public class SecurityGetName {
    public static String getUsername() {
        //如果登录了，name即用户名；如果没有登录，默认为 anonymousUser
        //方法一、
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = myUserDetails.getEmail(); //主体名，即登录用户名
//        System.out.println("当前用户名："+email);//saysky 或 anonymousUser
        return email;
    }
}
