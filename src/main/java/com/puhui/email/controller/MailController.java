package com.puhui.email.controller;


import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Message;
import com.puhui.email.entity.Role;
import com.puhui.email.service.MailService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.MessageService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.AesUtil;
import com.puhui.email.util.BaseResult;
import com.puhui.email.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: 邹玉玺
 * @date: 2020/7/5-15:54
 */
@RestController
@Slf4j
@Api (value = "邮件发送controller", tags = {"邮件发送接口"})
public class MailController {

    private static BaseResult result = new BaseResult("", "");
    @Autowired
    private RedisTemplate<String, String> redisTemplates;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailUserService mailUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MailUserService userService;
    @Autowired
    private MessageService messageService;

    /**
     * 指定用户发送邮件接口
     *
     * @param target           邮件发送目标姓名
     * @param topic            邮件主题
     * @param content          邮件内容
     * @param multipartFile    添加的附件
     * @param sendTemplateMail 是否使用模板发送邮件（默认不使用）
     * @param sendMessage      是否同时发送短信（默认不发送）
     * @return
     * @throws Exception
     */
    @ApiOperation ("指定用户发送邮件")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "target", value = "目标用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "topic", value = "邮件主题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "content", value = "邮件内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "sendTemplateMail", value = "是否使用模板发送", required = false, dataType = "Boolean", paramType = "query", defaultValue = "false"),
            @ApiImplicitParam (name = "sendMessage", value = "是否发送通知短信", required = false, dataType = "Boolean", paramType = "query", defaultValue = "false")
    })
    @PostMapping ("/mail/sendMail")
    public BaseResult sendSimpleMail(String target, String topic, String content, MultipartFile multipartFile, Boolean sendTemplateMail, Boolean sendMessage) throws Exception {
        //根据用户名查询用户(数据是加密的)
        MailUser user = mailUserService.queryUserByName(target);
        String filePath = null;
        if (user != null) {
            //封装短信信息
            Message message = new Message();
            //收件人电话号码(需要解密)
            message.setTargetphone(AesUtil.decrypt(user.getPhone()));
            //收件人姓名
            message.setTarget(target);
            //邮件内容
            message.setContent(content);
            //新建一个邮件对象
            MailRecord mailRecord = new MailRecord();
            //获取  redis数据库 中对用户名缓存的标识码
            String redisNameCode = redisTemplates.opsForValue().get(user.getName());
            //获取 redis数据库  中对邮箱号缓存的标识码
            String redisEmailCode = redisTemplates.opsForValue().get(user.getEmail());
            //获取  redis数据库 中对电话号码缓存的标识码
            String phoneCode = redisTemplates.opsForValue().get(AesUtil.decrypt(user.getPhone()));
            //判断该用户名30分钟之内是否已经提交过操作
            if (redisNameCode != null) {
                if (sendMessage) {
                    if (phoneCode != null) {
                        //都不能发送
                        result.setSuccess(false);
                        result.setMessage("您的邮件和短信发送频率过高，请稍后再试");
                        return result;
                    }
                    //邮件发送失败，短信发送成功
                    messageService.sendMessage(message);
                    result.setMessage("您的邮件发送频率过高，请稍后再试；短信发送请求已提交");
                    return result;
                }
                result.setSuccess(false);
                result.setMessage("您的邮件发送频率过高，请稍后再试");
                return result;
            }
            //判断该邮箱当天是否已经成功发送过一次邮件
            if (redisEmailCode != null) {

                if (sendMessage) {
                    if (phoneCode != null) {
                        //都不能发送
                        result.setSuccess(false);
                        result.setMessage("您的邮件和短信发送频率过高，请稍后再试");
                        return result;
                    }
                    //邮件发送失败，短信发送成功
                    messageService.sendMessage(message);
                    result.setMessage("由于邮箱资源有限，同一用户邮箱每天只能发送一次邮件；短信发送请求已提交");
                    return result;
                }
                result.setSuccess(false);
                result.setMessage("由于邮箱资源有限，同一用户邮箱每天只能发送一次邮件");
                return result;
            }
            //保证能够发送邮件的情况下才去上传文件
            if (multipartFile != null) {
                filePath = FileUtil.fileUpload(multipartFile);
                //附件路径
                mailRecord.setFilepath(filePath);
            }
            if (sendMessage) {
                if (phoneCode != null) {
                    //邮件能发送，短信不能发送
                    mailService.sendSimpleMail(user, mailRecord, sendTemplateMail);
                    result.setSuccess(false);
                    result.setMessage("您的邮件发送请求已提交；短信发送频率过高，请稍后再试");
                    return result;
                }
                //都能发送
                mailService.sendSimpleMail(user, mailRecord, sendTemplateMail);
                messageService.sendMessage(message);
                result.setSuccess(true);
                result.setMessage("已提交邮件发送请求和短信发送请求");
                return result;
            }
            mailService.sendSimpleMail(user, mailRecord, sendTemplateMail);
            result.setSuccess(true);
            result.setMessage("已提交邮件发送");
            return result;
        }
        //用户不存在的情况下，对结果集进行封装返回
        result.setSuccess(false);
        result.setMessage("该用户不存在，请核对用户名是否正确");
        return result;
    }

    /**
     * 根据角色名发送邮件给扮演该角色的所有用户
     *
     * @param roleNote
     * @param content
     * @param topic
     * @return
     */
    @ApiOperation ("根据角色发送邮件")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "roleNote", value = "角色名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "topic", value = "邮件主题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "content", value = "邮件内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "sendTemplateMail", value = "是否使用模板发送", required = false, dataType = "Boolean", paramType = "query", defaultValue = "false")
            // @ApiImplicitParam (name = "sendMessage", value = "是否发送短信", required = false, dataType = "Boolean", paramType = "query", defaultValue = "false")
    })
    @PostMapping ("/mail/sendEmailByRole")
    public BaseResult sendEmailByRole(String roleNote, String content, String topic, Boolean sendTemplateMail) {
        BaseResult result = new BaseResult("", "");
        try {
            //根据角色查询角色信息
            Role role = roleService.roleSelectNote(roleNote);
            //如果角色为空，角色不存在
            if (role == null) {
                result.setCode("1");
                result.setSuccess(false);
                result.setMessage("发送失败，该角色不存在");
            } else {
                //角色存在
                MailRecord mailRecord = new MailRecord();
                //根据角色关联查询扮演该角色的所有用户
                List<MailUser> users = mailUserService.mailUserSelectByRole(role.getId());

                ListOperations operations = redisTemplate.opsForList();

              if (sendTemplateMail){
                  //将角色名放入队列

                  operations.leftPush("sendTemplateMailByRole", sendTemplateMail);
              }

                //将该角色下所有用户放入队列中
                users.forEach(str -> {
                    mailRecord.setTarget(str.getEmail());
                    mailRecord.setContent(content);
                    mailRecord.setTopic(topic);

                    //将sendTemplateMail放入队列,一个用户对应一个角色
                    operations.leftPush("role", roleNote);
                    operations.leftPush("mailRecord", mailRecord);
                });
                result.setCode("0");
                result.setSuccess(true);
                result.setMessage("邮件发送任务提交成功");
            }
        } catch (Exception e) {
            result.setCode("1");
            result.setSuccess(false);
            result.setMessage("发送失败");
        }
        return result;
    }


}
