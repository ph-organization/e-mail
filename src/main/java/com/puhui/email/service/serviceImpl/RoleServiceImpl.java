package com.puhui.email.service.serviceImpl;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.entity.RoleGroup;
import com.puhui.email.mapper.RoleMapper;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.LogicCRUDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 角色查询
 * @author: 杨利华
 * @date: 2020/7/9
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    MailUserService mailUserService;

    /**
     * //根据用户邮箱查询自己的角色
     * @param email
     * @return
     */
    @Override
    public List<Role> roleSelect(String email) {
        if (email == null || email.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值");
            return null;
        } else {
            MailUser mailUser = mailUserService.mailUserSelect(email);
            //判断用户是否有效
            if (mailUser == null) {
                return null;
            } else {
                List<Role> allRole=new ArrayList<Role>();
                //用户角色
                List<Role> userRole = roleMapper.roleSelect(email);
                //用户所在组角色
                List<Role> roleGroupRole=roleMapper.roleSelectByEmail(email);
                //合并
                allRole.addAll(userRole);
                allRole.addAll(roleGroupRole);
                //去重
                allRole=new ArrayList<Role>(new LinkedHashSet<>(allRole));
                //返回有效用户的角色
                return allRole;
            }
        }
    }

    /**
     * //根据角色名查询
     * @param roleName
     * @return
     */
    @Override
    public Role roleSelectNote(String roleName) {
        if (roleName == null || roleName.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值");
            return null;
        } else {
            return roleMapper.roleSelectNote(roleName);
        }
    }

    /**
     * //根据角色名查询该角色下的所有用户
     * @param roleName
     * @return
     */
    @Override
    public List<MailUser> roleSelectRelation(String roleName) {
        if (roleName == null || roleName.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值");
            return null;
        } else {
            //那到拥有该角色的所有用户
            List<MailUser> allMailUser=new ArrayList<MailUser>();
            List<MailUser> mList = roleMapper.roleSelectUser(roleName);
            //通过角色名拿到拥有该角色的所有用户（通过角色组）
            List<MailUser> roleGroupMailUsers=mailUserSelectByRoleName(roleName);
            //合并集合
            allMailUser.addAll(mList);
            allMailUser.addAll(roleGroupMailUsers);
            //去除重复的邮件用户
            allMailUser=new ArrayList<MailUser>(new LinkedHashSet<>(allMailUser));
            return allMailUser.stream().filter(mailUser -> "true".equals(mailUser.getLose_user())).collect(Collectors.toList());
        }
    }

    /**
     * 根据邮箱查询角色（通过角色组查询）
     *
     * @param email
     * @return
     */
    @Override
    public List<Role> roleSelectByEmail(String email) {
        if (email == null || "".equals(email.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            MailUser mailUser = mailUserService.mailUserSelect(email);
            //判断用户是否有效
            if (mailUser == null) {
                return null;
            } else {
                //返回有效用户的角色集合
                List<Role> roleList = roleMapper.roleSelectByEmail(email);
                return roleList;
            }
        }
    }

    /**
     * 根据角色组查询（主要拿到角色组id）
     * @param roleGroupName
     * @return
     */
    @Override
    public RoleGroup roleGroupSelectByRoleGroupName(String roleGroupName) {
        if (roleGroupName == null || "".equals(roleGroupName.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            return roleMapper.roleGroupSelectByRoleGroupName(roleGroupName);
        }

    }

    /**
     * 根据角色名查询角色下的所有邮件用户（通过角色组查询）
     * @param roleName
     * @return
     */
    @Override
    public List<MailUser> mailUserSelectByRoleName(String roleName) {
        if (roleName == null || "".equals(roleName.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            return roleMapper.mailUserSelectByRoleName(roleName);
        }
    }
}
