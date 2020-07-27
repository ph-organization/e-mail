package com.puhui.email.mapper;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.entity.RoleGroup;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RoleMapper {
    /**
     * //根据用户邮箱查询角色
     * @param user_email
     * @return
     */
    public List<Role> roleSelect(String user_email);

    /**
     * //根据角色名查询
     * @param roleName
     * @return
     */
    Role roleSelectNote(String roleName);

    /**
     *  根据角色名查询该角色下的所有用户
     * @param roleName
     * @return
     */
    List<MailUser> roleSelectUser(String roleName);

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
