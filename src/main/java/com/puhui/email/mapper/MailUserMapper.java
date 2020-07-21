package com.puhui.email.mapper;

import com.puhui.email.entity.MailUser;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/5
 */

public interface MailUserMapper {

    //管理员 start
    //添加用户
    void mailUserInsert(MailUser mailUser);

    //逻辑删除用户
    void mailUserLogicDelete(MailUser mailUser);

    //逻辑删除多个用户
    void mailUserLogicDeleteS(List<Integer> ids);

    //修改用户
    void mailUserUpdate(MailUser mailUser);

    //添加逻辑删除后的用户(重启逻辑删除后的用户)
    void mailUserRestart(MailUser mailUser);

    //根据邮箱查询用户
    MailUser mailUserSelect(String email);

    //根据邮箱模糊查询用户
    List<MailUser> mailUserDimSelect(String email);

    //根据id查询用户
    List<MailUser> mailUserSelectById(List<Integer> ids);

    //根据角色查询该角色下的所有用户
    List<MailUser> mailUserSelectByRole(Integer role_id);

    //    List<MailUser> mailUserSelects(List<String> email);
    //查询全部
    List<MailUser> mailUserSelectAll();

    //根据用户姓名查询用户
    MailUser queryUserByName(String name);

// 管理员 end

}
