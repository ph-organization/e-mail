package com.puhui.email.service.serviceimpl;

import com.puhui.email.entity.Message;
import com.puhui.email.mapper.MessageMapper;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.MessageService;
import com.puhui.email.util.CommonUtil;
import com.puhui.email.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-14:57
 * <p>
 * 短信的相关实现类
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private RedisTemplate<String, Object> template;

    @Autowired
    private MailUserService mailUserService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageUtil messageUtil;

    //发送短信
    @Override
    @Async ("taskExecutors")
    public void sendMessage(Message message) {
        //获取登录用户的email信息
        String loginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
        //获取一个随机的标识码，用于标记该用户电话
        String phoneCode = CommonUtil.getRandomNum();
        try {
            //发送短信
            message = messageUtil.sendMessage(message);
            //将用户电话号码对应的缓存符放入到redis数据库   设置30分钟失效
            template.opsForValue().set(message.getTargetphone(), phoneCode, 30, TimeUnit.MINUTES);

            message.setSender(mailUserService.mailUserSelect(loginUserEmail).getName());
            //保存短信发送记录
            messageMapper.saveMessage(message);

            log.info("成功保存短信发送记录");
        } catch (Exception e) {
            log.info("发送失败");
        }
    }

    //查询当前用户发送短信记录
    @Override
    public List<Message> listMessage() {
        //获取登录用户的email信息
        String loginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
        //根据email查询到用户名
        String sender = mailUserService.mailUserSelect(loginUserEmail).getName();
        //根据sender查询对应的发送短信记录
        List<Message> messages = messageMapper.listMessage(sender);
        return messages;
    }

    //删除当前用户所有短信记录
    @Override
    public void deleteMessage() {
//获取登录用户的email信息
        String loginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
        //根据email查询到用户名
        String sender = mailUserService.mailUserSelect(loginUserEmail).getName();
        //根据sender删除对应所有记录
        messageMapper.deleteMessage(sender);
    }

    //删除指定短信记录
    @Override
    public void deleteById(Integer id) {
        messageMapper.deleteById(id);

    }


}
