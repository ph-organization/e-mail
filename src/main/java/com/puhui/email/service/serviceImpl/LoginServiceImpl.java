package com.puhui.email.service.serviceImpl;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.MyUserDetails;
import com.puhui.email.mapper.LoginMapper;
import com.puhui.email.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
            myUserDetails.setRoles(loginMapper.getUserRole(myUserDetails.getId()));
            log.info("detalis"+myUserDetails);
        }
        //将用户存入缓存
        redisTemplate.opsForValue().set("LoginUserEmail",myUserDetails.getEmail());
        return myUserDetails;
    }
}
