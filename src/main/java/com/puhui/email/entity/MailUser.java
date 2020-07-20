package com.puhui.email.entity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 邮件用户实体
 * @author: 杨利华
 * @date: 2020/7/4
 */
@Data   //相当于加入getset方法
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //无参构造
@ToString   //重写tostring方法，引入Lombok简化代码
@Table(name = "mail_user")
//解决id查询对象出现异常
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class MailUser implements Serializable {

    //用户id
    @ApiParam("用户名id(添加用户不填)")
    private int id;
    //用户姓名
    @ApiParam("用户名")
    private String name;
    //用户性别
    @ApiParam("用户性别")
    private String sex;
    //出生年月
    @ApiParam("用户出生日期（2020-01-02）")
    private String birthday;
    //邮件
    @ApiParam("邮箱")
    private String email;
    //手机号
    @ApiParam("手机号")
    private String phone;
    //密码
    @ApiParam("登录密码")
    private String pwd;
    //地址
    @ApiParam("家庭住址")
    private String address;
    //账户创建时间
    @ApiParam("账户创建时间")
    private String create_time;
    //账户修改时间
    @ApiParam("账户修改时间")
    private String update_time;
    //账号标记是否失效
    @ApiParam("账户标记是否失效")
    private String lose_user;


    //用于测试修改用户name,sex,birthday,email,phone,pwd,address
    public MailUser(String name, String sex, String birthday, String email, String phone, String pwd, String address) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
        this.address = address;
    }

    //用户逻辑删除用户
    public MailUser(String name, String sex, String birthday, String email, String phone, String pwd, String address, String lose_user) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
        this.address = address;
        this.lose_user = lose_user;
    }
}
