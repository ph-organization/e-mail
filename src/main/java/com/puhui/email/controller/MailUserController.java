package com.puhui.email.controller;


import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.MailUserList;
import com.puhui.email.entity.Relation;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RelationService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.SecurityGetName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: controller的curd
 * @author: 杨利华
 * @date: 2020/7/5
 */
@RestController
@Slf4j
@Api(tags={"普通管理员接口"})
@RequestMapping(value = "/admin")
public class MailUserController {

    //邮件用户实体
    @Resource
    MailUserService mailUserService;
    //邮件记录实体
    @Resource
    MailRecordService mailRecordService;

    //添加用户
    @ApiOperation("用户添加")
    @PostMapping(value = "/addUser")
    //在swagger页面添加注释
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "birthday", value = "生日(2020-03-02)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "号码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "家庭地址", required = false, dataType = "String", paramType = "query")
    })
    public void addMailUser(String name, String sex, String birthday, String email, String phone, String pwd, String address) {
        MailUser mailUser = new MailUser(name, sex, birthday, email, phone, pwd, address);
        //返回失效用户
        MailUser mailUserFailed = mailUserService.mailUserFailed(email);
        //判断该Email是否是失效用户
        if (mailUserFailed != null && mailUserFailed.getEmail().equals(email)) {
            //重启失效用户
            mailUserService.mailUserRestart(mailUser);
        } else {
            //添加用户
            mailUserService.mailUserInsert(mailUser);
        }
    }

    //逻辑删除用户
    @ApiOperation("根据Email删除")
    @PutMapping(value = "/deleteByEmail")
    @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query")
    public void deleteMailUser(String email) {
        mailUserService.mailUserLogicDelete(email);
    }

    //逻辑删除多个用户
    @ApiOperation("根据id逻辑删除多个用户")
    @PutMapping(value = "/deleteS")
    public void deleteMailUsers(@RequestParam List<Integer> ids) {
        mailUserService.mailUserLogicDeleteS(ids);
    }

    //修改单个用户
    @ApiOperation("修改单个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "birthday", value = "生日(2020-03-02)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "号码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "家庭地址", required = false, dataType = "String", paramType = "query")
    })
    @PutMapping(value = "/upDateUser")
    public void upDateMailUser(String name, String sex, String birthday, String email, String phone, String pwd, String address) {
        mailUserService.mailUserUpdate(new MailUser(name, sex, birthday, email, phone, pwd, address));
    }

    //修改多个用户
    @ApiOperation("修改多个用户")
    @PutMapping(value = "/upDateUserS")
    public void upDateMailUsers(@RequestBody MailUserList list) {
        for (MailUser mu : list.getMailUserList()) {
            mailUserService.mailUserUpdate(mu);
        }
    }

    //根据邮箱模糊查询用户
    @ApiOperation("邮箱模糊查询用户的信息")
    @GetMapping(value = "/queryDimSelect")
    public List<MailUser> queryDimSelect(String email) {
        return mailUserService.mailUserDimSelect(email);
    }

    //查询多个
    @ApiOperation("根据id查询多个")
    @GetMapping(value = "/queryByIds")
    public List<MailUser> queryMailUsers(@RequestParam(value = "ids") List<Integer> ids) {
        return mailUserService.mailUserSelectById(ids);
    }

    //查询全部
    @ApiOperation("查询全部")
    @GetMapping(value = "/queryAll")
    public List<MailUser> queryMailUsersAll() {
        return mailUserService.mailUserSelectAll();
    }


}
