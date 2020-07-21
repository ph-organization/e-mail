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

    MailUser loadUserByEmail(String email);

    List<Role> getUserRole(Integer id);

}
