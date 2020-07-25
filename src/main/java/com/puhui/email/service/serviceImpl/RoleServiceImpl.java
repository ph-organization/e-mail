package com.puhui.email.service.serviceImpl;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.mapper.RoleMapper;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.LogicCRUDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    //根据用户邮箱查询自己的角色
    @Override
    public List<Role> roleSelect(String email) {
        if (email == null || "".equals(email.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            MailUser mailUser = mailUserService.mailUserSelect(email);
            //判断用户是否有效
            if (mailUser == null) {
                return null;
            } else {
                //返回有效用户的角色
                return roleMapper.roleSelect(email);
            }
        }
    }

    //根据角色名查询
    @Override
    public Role roleSelectNote(String roleName) {
        if (roleName == null || "".equals(roleName.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            return roleMapper.roleSelectNote(roleName);
        }
    }

    //根据角色名查询该角色下的所有用户
    @Override
    public List<MailUser> roleSelectRelation(String roleName) {
        if (roleName == null || "".equals(roleName.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            //那到拥有该角色的所有用户
            List<MailUser> mList = roleMapper.roleSelectUser(roleName);
            return mList.stream().filter(mailUser -> "true".equals(mailUser.getLose_user())).collect(Collectors.toList());
        }
    }
}
