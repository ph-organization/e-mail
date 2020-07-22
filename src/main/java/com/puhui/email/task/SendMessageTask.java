package com.puhui.email.task;

import com.puhui.email.entity.Message;
import com.puhui.email.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: 邹玉玺
 * @date: 2020/7/22-17:58
 *
 * 发送短信的定时任务
 */
@Component
@Slf4j
public class SendMessageTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageService messageService;
    /**
     * 根据角色发送短信   从队列取出消息体
     */
    @Scheduled (cron = "*/10 * * * * ?")
    public void sendMessageByRole(){
        //定时的查看队列是否有消息体存入
        ListOperations operations = redisTemplate.opsForList();

        while (true){
            Message message =(Message) operations.rightPop("Message");
            if (message==null){
                //当队列中取出完，跳出循环
                break;
            }
            //调用发送短信接口
            log.info(message+"");
//            messageService.sendMessage(message);
        }


    }

}
