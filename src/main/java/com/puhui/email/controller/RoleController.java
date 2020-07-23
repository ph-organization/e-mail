package com.puhui.email.controller;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Relation;
import com.puhui.email.entity.Role;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RelationService;
import com.puhui.email.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 角色crud 与 关联表的增删查
 * @author: 杨利华
 * @date: 2020/7/9
 */
@RestController
@Slf4j
@Api(tags={"超级管理员接口"})
@RequestMapping(value = "/superAdmin")
public class RoleController {

    @Resource
    RelationService relationService;
    @Resource
    MailUserService mailUserService;
    @Resource
    RoleService roleService;
    @Resource
    MailRecordService mailRecordService;

    ////根据角色名查询该角色下的所有用户
    @ApiOperation(value = "根据角色名查询该角色下的所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "权限名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/roleSelectRelation")
    public List<MailUser> roleSelectRelation(String roleName) {
        List<MailUser> relations = roleService.roleSelectRelation(roleName);
        return relations;
    }

    //查询角色
    @ApiOperation(value = "根据用户邮箱查询该用户担任的所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Email", value = "邮箱", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/roleSelect")
    public List<Role> roleSelect(String Email) {
        return roleService.roleSelect(Email);
    }


    //添加关系
    @ApiOperation(value = "根据Email添加关联")
    @PostMapping(value = "/relationInsert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mailUsrEmail", value = "用户邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleName", value = "权限名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    public void relationInsert(String mailUsrEmail, String roleName) {
        //先查寻用户id和角色id 在赋值添加关系
        MailUser mailUser = mailUserService.mailUserSelect(mailUsrEmail);
        Role role = roleService.roleSelectNote(roleName);
        if (mailUser == null || role == null) {
            return;
        } else {
            relationService.relationInsert(new Relation(mailUser.getId(), role.getId()));
        }
    }

    //根据用户邮箱删除与某角色关联
    @ApiOperation(value = "根据用户邮箱删除关联")
    @DeleteMapping(value = "/relationDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mailUsrEmail", value = "用户邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleName", value = "权限名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    public void relationDelete(String mailUsrEmail, String roleName) {
        //根据用户名查询用户id，再根据用户id删除关系
        MailUser mailUser = mailUserService.mailUserSelect(mailUsrEmail);
        Role role = roleService.roleSelectNote(roleName);
        if (mailUser == null || role == null) {
            return;
        } else {
            relationService.relationDelete(new Relation(mailUser.getId(), role.getId()));
        }
    }

    //根据邮箱删除邮件记录
    @ApiOperation(value = "根据邮箱删除邮件记录")
    @DeleteMapping(value = "/mailRecordDelete")
    public void mailRecordDelete(String email) {
        mailRecordService.mailRecordDelete(email);
    }

    //根据邮箱查询邮件记录
    @ApiOperation(value = "根据邮箱查询邮件记录")
    @GetMapping(value = "/mailRecordInsert")
    public List<MailRecord> mailRecordInsert(String email) {
        return mailRecordService.mailRecordSelect(email);
    }

}
