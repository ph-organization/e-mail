package com.puhui.email.mapper;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/10
 */
public interface LoginMapper {
    /**
     * 拿到登录用户
     * @param email
     * @return
     */
    MailUser loadUserByEmail(String email);

    /**
     * 拿到登录用户权限
     * @param id
     * @return
     */
    List<Role> getUserRole(Integer id);

}
