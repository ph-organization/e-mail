package com.puhui.email;


import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.mapper.MailUserMapper;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.AESUtil;
import com.puhui.email.util.TimesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith (SpringRunner.class)
@Transactional
@Rollback
public class EMailApplicationTests {
    @Resource
    MailUserMapper mailUserMapper;

    /**
     * 邮件用户的crud，redis记录测试
     * 杨利华
     */
    @Autowired
    MailUserService mailUserService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RoleService roleService;

    //添加用户
    @Test
    public void ylhAddUserTest(){
        //添加用户
        MailUser user = new MailUser("杨华", "男", "2020-01-02", "253478376@qq.com", "15285360367", "123456", "北京");
        mailUserService.mailUserInsert(user);
    }


    //逻辑删除用户
    @Test
    public void ylhDeleteUserTest() throws Exception {
        //根据id删除
        mailUserService.mailUserLogicDelete(("25378376@qq.com"));
    }

    //修改用户
    @Test
    public void ylhUpdateUserTest(){
        MailUser user = new MailUser("杨利华", "男", "2020-01-02", "25378376@qq.com", "15285360367", "123456", "北京");
        //修改用户(修改email相同的用户)
        mailUserService.mailUserUpdate(user);
    }

    //查询用户
    @Test
    public void ylhQueryUser() throws Exception {
        //根据email查询
        mailUserService.mailUserSelectAll();
    }

    /**
     * 根据角色名查出角色对应信息
     */
    @Test
    public void queryRoleByName(){
        List<Role> role = roleService.roleSelect("2235662296@qq.com");
        System.out.println(role);
    }

    /**
     * 测试
     * 根据角色Id查询出该角色下所有用户
     */
    @Test
    public void queryUsersByRole(){
        List<MailUser> mailUsers = mailUserService.mailUserSelectByRole(2);
        System.out.println(mailUsers);
    }

    //加密解密
    @Test
    public void AEStest(){
        MailUser user = new MailUser("邹玉玺", "男", "1996-07-06", "1003941268@qq.com", "15680680103", "123456", "成都");
        MailUser encryptedUser = AESUtil.encryptUser(user);
        MailUser encryptedUser1 = AESUtil.encryptUser(user);
        System.out.println("加密后的对象是"+user);
        System.out.println("加密后的对象是2"+user);
        MailUser deUser = AESUtil.decryptUser(encryptedUser);
        System.out.println("解密后的对象是"+deUser);
        System.out.println(AESUtil.decrypt("/24/Aeg+9+Luk0o5P+Cukg=="));
    }

@Test
    public void redisTest(){
        MailUser user = new MailUser("杨利华", "男", "2020-01-02", "25378376@qq.com", "15285360367", "123456", "北京");
        redisTemplate.opsForValue().set(user.getName(),user);
    MailUser mailUser =(MailUser) redisTemplate.opsForValue().get(user.getName());
    System.out.println(mailUser);
}
    @Test
    public void redisTest2(){
        System.out.println("j解密："+AESUtil.decrypt("53E79812A1D7AE227F6845C049136526"));
}


}
