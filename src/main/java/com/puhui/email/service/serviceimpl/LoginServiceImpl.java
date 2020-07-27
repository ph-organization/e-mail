package com.puhui.email.service.serviceimpl;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.MyUserDetails;
import com.puhui.email.entity.Role;
import com.puhui.email.mapper.LoginMapper;
import com.puhui.email.service.LoginService;
import com.puhui.email.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/10
 */

@Service
@Slf4j

public class LoginServiceImpl implements LoginService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    LoginMapper loginMapper;
    @Resource
    MyUserDetails myUserDetails;
    @Resource
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MailUser mailUser = loginMapper.loadUserByEmail(email);
        myUserDetails.setId(mailUser.getId());
        myUserDetails.setEmail(mailUser.getEmail());
        myUserDetails.setPwd(mailUser.getPwd());
        myUserDetails.setLose_user(mailUser.getLose_user());
        if(myUserDetails==null){
            throw new UsernameNotFoundException("账户不存在");
        }else {
            List<Role> allRole=new ArrayList<Role>();
            //通过角色组查询到的角色集
            List<Role> roleList = roleService.roleSelectByEmail(email);
            //通过邮件用户与角色查询到的角色集
            List<Role> userRole = loginMapper.getUserRole(myUserDetails.getId());
            //合并角色集
            allRole.addAll(roleList);
            allRole.addAll(userRole);
            //去除重复的角色
            allRole=new ArrayList<Role>(new LinkedHashSet<>(allRole));
            myUserDetails.setRoles(allRole);
            log.info("detalis"+myUserDetails);
        }
        //将用户存入缓存
        redisTemplate.opsForValue().set("LoginUserEmail",myUserDetails.getEmail());
        return myUserDetails;
    }
}
