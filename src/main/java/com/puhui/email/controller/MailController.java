package com.puhui.email.controller;


import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Role;
import com.puhui.email.service.MailService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.BaseResult;
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
@Api (value="邮件发送controller",tags={"邮件发送接口"})
public class MailController {

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
    private static BaseResult result = new BaseResult("", "");

    /**
     * 发送邮件接口
     *
     * @param target  目标地址
     * @param topic   邮件主题
     * @param content 邮件内容
     */
    @ApiOperation ("普通邮件发送")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target", value = "目标用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "topic", value = "邮件主题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "邮件内容", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping ("/mail/sendMail")
    public BaseResult sendSimpleMail(String target, String topic, String content, MultipartFile multipartFile) throws Exception {

        //根据用户名查询用户
        MailUser user = mailUserService.queryUserByName(target);

        if (user!=null){

            //获取  redis数据库 中对用户名缓存的标识码
            String redisNameCode = redisTemplates.opsForValue().get(user.getName());

            //获取 redis数据库  中对邮箱号缓存的标识码
            String redisEmailCode = redisTemplates.opsForValue().get(user.getEmail());

            //判断该用户名30分钟之内是否已经提交过操作
            if (redisNameCode != null) {
                //用户名对应缓存的标识符存在的话，不能发送邮件
                result.setCode("1");
                result.setSuccess(false);
                result.setMessage("30分钟内只能发送一次，请30分钟后再次操作");
                return result;
            }
            //判断该邮箱当天是否已经成功发送过一次邮件
            if (redisEmailCode !=null){
                result.setCode("1");
                result.setSuccess(false);
                result.setMessage("由于邮箱资源有限，同一用户邮箱每天只能发送一次邮件");
                return result;
            }
            mailService.sendSimpleMail(user,topic,content,multipartFile);
            result.setSuccess(true);
            result.setCode("0");
            result.setMessage("已提交邮件发送请求");
            log.info("提交请求成功");
            return result;
        }
            //用户不存在的情况下，对结果集进行封装返回
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("该用户不存在，请核对用户名是否正确");
        return result;
    }

    /**
     * 根据角色名发送邮件给扮演该角色的所有用户
     * @param roleName
     * @param content
     * @param topic
     * @return
     */
    @ApiOperation ("根据角色发送邮件")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "topic", value = "邮件主题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "邮件内容", required = true, dataType = "String", paramType = "query"),

    })
    @PostMapping ("/mail/sendMailByRole")
    public BaseResult sendEmailByRole(String roleName, String content, String topic){
        BaseResult result = new BaseResult("","");
        try {
            //根据角色查询角色信息
            Role role = roleService.roleSelectNote(roleName);
            if (role==null){
                result.setCode("1");
                result.setSuccess(false);
                result.setMessage("发送失败，该角色不存在");
            }else {
                MailRecord mailRecord=new MailRecord();
                //根据角色关联查询扮演该角色的所有用户
                List<MailUser> users = mailUserService.mailUserSelectByRole(role.getId());

                ListOperations operations = redisTemplate.opsForList();
                //将该角色下所有用户放入队列中
                for (MailUser user : users) {
                     mailRecord.setEmail(user.getEmail());
                     mailRecord.setContent(content);
                     mailRecord.setTopic(topic);
                    operations.leftPush("mailRecord", mailRecord);
                }
                result.setCode("0");
                result.setSuccess(true);
                result.setMessage("发送任务提交成功");
            }
        } catch (Exception e) {
            result.setCode("1");
            result.setSuccess(false);
            result.setMessage("发送失败");
        }
        return result;
    }




}
