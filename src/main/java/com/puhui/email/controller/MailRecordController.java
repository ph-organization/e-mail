package com.puhui.email.controller;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.util.SecurityGetName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 邹玉玺
 * @date: 2020/7/16-16:16
 */
@Slf4j
@RestController
@Api (value="邮件记录管理controller",tags={"邮件记录管理接口"})
public class MailRecordController {
    @Autowired
    private MailRecordService mailRecordService;

    //根据当前登录的邮箱删除邮件记录
    @ApiOperation(value = "删除全部邮件记录")
    @DeleteMapping(value = "/mail/mailRecordDelete")
    public void mailRecordDelete() {
        //获取当前用户名
        String mailUsrEmail= SecurityGetName.getUsername();
        mailRecordService.mailRecordDelete(mailUsrEmail);
    }


    @ApiOperation (value = "删除单条邮件记录")
    @DeleteMapping (value = "/mail/mailRecordTopicDelete")
    @ApiImplicitParams ({@ApiImplicitParam (name = "id", value = "邮件记录对应id",  dataType = "Integer", paramType = "query")
    })
    public void mailRecordTopicDelete(@RequestParam(value = "id") Integer id) {
        log.info("需要删除的邮件记录id是"+id);
        mailRecordService.deleteMailRecordById(id);
    }

    //根据当前登录的邮箱查询邮件记录
    @ApiOperation(value = "邮件记录表")
    @GetMapping (value = "/mail/mailRecordInsert")
    public List<MailRecord> mailRecordInsert() {
        //获取当前用户名
        String mailUsrEmail= SecurityGetName.getUsername();
        return mailRecordService.mailRecordSelect(mailUsrEmail);
    }

    //查询回收站（已删除的邮件记录）
    @ApiOperation(value = "回收站(删除的邮件记录会放入回收站7天有效)")
    @GetMapping (value = "/mail/maillRecycleBin")
    public List<MailRecord> maillRecycleBin(){
        //获取当前用户名
        String mailUsrEmail= SecurityGetName.getUsername();
        //根据当前邮件7天内已删除的邮件记录
        return mailRecordService.maillRecycleBin(mailUsrEmail);
    }


    //查询回收站（已删除的邮件记录）
    @ApiOperation(value = "垃圾箱（发送失败的邮件会存入垃圾箱）")
    @GetMapping (value = "/mail/mailRubbish")
    public List<MailRecord> mailRubbish(){
        //获取当前用户名
        String mailUsrEmail= SecurityGetName.getUsername();
        //根据当前邮件7天内已删除的邮件记录
        return mailRecordService.mailRubbish(mailUsrEmail);
    }
}
