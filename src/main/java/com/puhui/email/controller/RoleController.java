package com.puhui.email.controller;

import com.puhui.email.entity.*;
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

    @ApiOperation(value = "根据角色名查询该角色下的所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "权限名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/roleSelectRelation")
    public List<MailUser> roleSelectRelation(String roleName) {
        List<MailUser> roleMailUser = roleService.roleSelectRelation(roleName);
        return roleMailUser;
    }

    @ApiOperation(value = "根据用户邮箱查询该用户担任的所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Email", value = "邮箱", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/roleSelect")
    public List<Role> roleSelect(String Email) {
        return roleService.roleSelect(Email);
    }


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
        if (!(mailUser == null || role == null)) {
            relationService.relationInsert(new Relation(mailUser.getId(), role.getId()));
        }
    }

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
        if (!(mailUser == null || role == null)) {
            relationService.relationDelete(new Relation(mailUser.getId(), role.getId()));
        }
    }

    @ApiOperation(value = "添加邮件用户与角色组的关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(name = "/roleGroupRelationAdd")
    public void roleGroupRelationAdd(String email, String roleGroupName) {
        MailUser mailUser = mailUserService.mailUserSelect(email);
        RoleGroup roleGroup = roleService.roleGroupSelectByRoleGroupName(roleGroupName);
        if (!(mailUser == null || roleGroup == null)) {
            relationService.roleGroupRelationAdd(new MailUseRelationRoleGroup(mailUser.getId(), roleGroup.getId()));
        }
    }

    @ApiOperation(value = "删除与角色组的关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名（superAdmin,admin,user）", required = false, dataType = "String", paramType = "query")
    })
    @DeleteMapping(name = "/roleGroupRelationDelete")
    public void roleGroupRelationDelete(String email, String roleGroupName) {
        MailUser mailUser = mailUserService.mailUserSelect(email);
        RoleGroup roleGroup = roleService.roleGroupSelectByRoleGroupName(roleGroupName);
        if (!(mailUser == null || roleGroup == null)) {
            relationService.roleGroupRelationDelete(new MailUseRelationRoleGroup(mailUser.getId(), roleGroup.getId()));
        }
    }
}
