package com.puhui.email.controller;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.util.SecurityGetName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/21
 */
@RestController
@Api(tags={"当前用户接口"})
@RequestMapping(value = "/user")
public class UserController {

    //邮件用户实体
    @Resource
    MailUserService mailUserService;
    //邮件记录实体
    @Resource
    MailRecordService mailRecordService;
    //查询用户
    @ApiOperation("查询当前用户的信息")
    @GetMapping(value = "/queryByEmail")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin','ROLE_superAdmin')")
    public MailUser queryMailUser() {
        //获取当前用户名
        String email = SecurityGetName.getUsername();
        return mailUserService.mailUserSelect(email);
    }

    //根据当前登录的邮箱删除邮件记录
    @ApiOperation(value = "根据当前登录的邮箱删除邮件记录")
    @DeleteMapping(value = "/mailRecordDelete")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin','ROLE_superAdmin')")
    public void mailRecordDelete() {
        //获取当前用户名
        String mailUsrEmail = SecurityGetName.getUsername();
        mailRecordService.mailRecordDelete(mailUsrEmail);
    }

    //根据当前登录的邮箱查询邮件记录
    @ApiOperation(value = "根据当前登录的邮箱查询邮件记录")
    @GetMapping(value = "/mailRecordInsert")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin','ROLE_superAdmin')")
    public List<MailRecord> mailRecordInsert() {
        //获取当前用户名
        String mailUsrEmail = SecurityGetName.getUsername();
        return mailRecordService.mailRecordSelect(mailUsrEmail);
    }
}
