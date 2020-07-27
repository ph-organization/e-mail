package com.puhui.email.controller;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.Message;
import com.puhui.email.entity.Role;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.MessageService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.AesUtil;
import com.puhui.email.util.BaseResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-14:05
 */
@RestController
@Slf4j
@Api (value = "短信发送controller", tags = {"短信接口"})
public class MessageController {
    private static BaseResult result = new BaseResult("", "");
    @Autowired
    private MailUserService mailUserService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplates;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 短信发送接口
     *
     * @param target  目标人
     * @param content 短信内容
     */
    @ApiOperation ("指定用户发送短信")
    @PostMapping ("/message/sendMessage")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "target", value = "短信接收人", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam (name = "content", value = "短信内容", required = true, dataType = "String", paramType = "query"),
    })
    public BaseResult sendMessage(String target, String content) {
        //根据target查询得到用户信息
        MailUser user = mailUserService.queryUserByName(target);

        //获取  redis数据库 中对电话号码缓存的标识码
        String phoneCode = (String) redisTemplates.opsForValue().get(AesUtil.decrypt(user.getPhone()));
        if (user != null) {
            //判断30分钟内是否发送过短信
            if (phoneCode != null) {
                result.setSuccess(false);
                result.setMessage("您的短信发送频率过高，请稍后再试");
                result.setCode("0");
                return result;
            }
            //新建一个短信对象
            Message message = new Message();
            //封装短信信息
            message.setTargetphone(AesUtil.decrypt(user.getPhone()));
            message.setTarget(target);
            message.setContent(content);

            //调用发送短信实现接口
            messageService.sendMessage(message);

            result.setSuccess(true);
            result.setMessage("短信发送请求已提交");
            result.setCode("0");
            return result;
        }
        result.setSuccess(false);
        result.setMessage("该用户不存在，请重新输入");
        result.setCode("1");
        //发送短信
        return result;
    }

    /**
     * 发送短信给指定角色下所有用户
     *
     * @param roleNote 角色名
     * @param content  短信内容
     * @return
     */
    @ApiOperation ("根据角色发送短信")
    @PostMapping ("/message/sendMessageByRole")
    public BaseResult sendMessageByRole(String roleNote, String content) {
        //根据角色命名查找角色是否存在
        Role role = roleService.roleSelectNote(roleNote);
        if (role != null) {
            Message message = new Message();
            //角色存在,查找角色对应所有用户
            List<MailUser> users = mailUserService.mailUserSelectByRole(role.getId());
            ListOperations operations = redisTemplate.opsForList();
            //将封装后的短信实体放入队列
            users.forEach(str -> {
                //接收人姓名
                message.setTarget(str.getName());
                //接收短信的电话号码
                message.setTargetphone(AesUtil.decrypt(str.getPhone()));
                //短信内容
                message.setContent(content);
                //将所有短信实体放入队列中

                operations.leftPush("Message", message);
            });
            result.setCode("0");
            result.setSuccess(true);
            result.setMessage("短信发送任务提交成功");
        }else {
            result.setCode("1");
            result.setSuccess(false);
            result.setMessage("短信发送失败，该角色不存在");
        }
        //角色不存在
        return result;
    }

    ;

    @ApiOperation ("查看发送短信记录")
    @GetMapping ("/message/listMessage")
    public List<Message> listMessage() {
        List<Message> messages = messageService.listMessage();
        return messages;
    }

    @ApiOperation ("删除所有短信记录")
    @DeleteMapping ("/message/deleteMessage")
    public BaseResult deleteMessage() {
        List<Message> messages = null;
        try {
            messages = messageService.listMessage();
            result.setSuccess(true);
            result.setMessage("删除成功");
            result.setCode("0");
        } catch (Exception e) {

            result.setSuccess(false);
            result.setMessage("删除失败，请重新提交");
            result.setCode("0");
        }
        return result;
    }


    @ApiOperation ("根据id删除指定短信记录")
    @ApiImplicitParam (name = "id", value = "短信id", required = true, dataType = "String", paramType = "query")
    @DeleteMapping ("/message/deleteById")
    public BaseResult deleteById(Integer id) {
        try {
            messageService.deleteById(id);
            result.setSuccess(true);
            result.setMessage("删除成功");
            result.setCode("0");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("删除失败，请重新提交");
            result.setCode("0");
        }
        return result;
    }

}
