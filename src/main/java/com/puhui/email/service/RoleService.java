package com.puhui.email.service;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;

import java.util.List;

/**
 * @description: 角色增删改
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RoleService {
    //根据用户邮箱查询自己的当前角色
    List<Role> roleSelect(String email);

    //根据角色名查询
    Role roleSelectNote(String roleName);

    //根据角色名查询该角色下的所有用户
    List<MailUser> roleSelectRelation(String roleName);
}
