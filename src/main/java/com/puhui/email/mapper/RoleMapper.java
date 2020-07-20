package com.puhui.email.mapper;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Relation;
import com.puhui.email.entity.Role;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RoleMapper {
    //根据用户邮箱查询角色
    public List<Role> roleSelect(String user_email);

    //根据角色名查询
    Role roleSelectNote(String roleName);

    //根据角色名查询该角色下的所有用户
    List<MailUser> roleSelectUser(String roleName);
}
