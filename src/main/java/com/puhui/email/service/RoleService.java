package com.puhui.email.service;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.entity.RoleGroup;

import java.util.List;

/**
 * @description: 角色增删改
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RoleService {
    /**
     * //根据用户邮箱查询自己的当前角色
     * @param email
     * @return
     */
    List<Role> roleSelect(String email);

    /**
     * //根据角色名查询
     * @param roleName
     * @return
     */
    Role roleSelectNote(String roleName);

    /**
     *     //根据角色名查询该角色下的所有用户
     * @param roleName
     * @return
     */
    List<MailUser> roleSelectRelation(String roleName);

    /**
     * 根据用户邮箱查询该用户拥有的角色集合（通过角色组查询）
     * @param email
     * @return
     */
    List<Role> roleSelectByEmail(String email);

    /**
     * 根据角色组名查询角色组（主要是拿到id）
     * @param roleGroupName
     * @return
     */
    RoleGroup roleGroupSelectByRoleGroupName(String roleGroupName);

    /**
     * 根据角色名查询角色下的所有邮件用户（通过角色组查询）
     * @param roleName
     * @return
     */
    List<MailUser> mailUserSelectByRoleName (String roleName);

    /**
     * 根据角色id查询角色下的所有邮件用户（通过角色组查询）
     * @param roleId
     * @return
     */
    List<MailUser> mailUserSelectByRoleId (Integer roleId);
}
