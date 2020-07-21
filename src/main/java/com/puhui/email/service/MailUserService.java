package com.puhui.email.service;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.MailUserList;

import java.util.List;

/**
 * @description: 邮件用户crud
 * @author: 杨利华
 * @date: 2020/7/5
 */
public interface MailUserService {
    //添加用户
    void mailUserInsert(MailUser mailUser);

    //逻辑删除用户
    void mailUserLogicDelete(String email);

    //逻辑删除多个用户
    void mailUserLogicDeleteS(List<Integer> ids);

    //修改用户
    void mailUserUpdate(MailUser mailUser);

    //查询用户
    MailUser mailUserSelect(String email);

    //查询全部
    List<MailUser> mailUserSelectAll();

    //根据id查询用户
    List<MailUser> mailUserSelectById(List<Integer> ids);

    //根据邮箱模糊查询用户
    List<MailUser> mailUserDimSelect(String email);

    //添加逻辑删除后的用户(重启逻辑删除后的用户)
    void mailUserRestart(MailUser mailUser);

    //根据角色查询该角色下的所有用户
    List<MailUser> mailUserSelectByRole(Integer role_id);

    //根据用户姓名查询用户
    MailUser queryUserByName(String name);

    //查询返回失效用户
    MailUser mailUserFailed(String email);
}
