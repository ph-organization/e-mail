package com.puhui.email.task;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.service.MailService;
import com.puhui.email.service.MailUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: 邹玉玺
 * @date: 2020/7/8-15:58
 * 定时任务
 */
@Component
@Slf4j
public class SendMailTask {
    @Autowired
    private MailUserService mailUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MailService mailService;
    private Logger logger = LoggerFactory.getLogger(SendMailTask.class);
    // cron接受cron表达式，根据cron表达式确定定时规则

    /**
     * 每天早晨9点准时给用户发送邮件  （内容自定义）
     */
    @Scheduled (cron = "0 */30 * * * ? ")
    public void sendEmailOnTime() {
        ListOperations opreation = redisTemplate.opsForList();
        //查询所有用户
        List<MailUser> mailUsers = mailUserService.mailUserSelectAll();
        //获取所有用户邮箱，放入队列
        for (MailUser user : mailUsers) {
            opreation.leftPush("target", user);
        }
        log.info("------------定时群发任务完成");

        log.info("-----------------------取出队列中的数据");

        while (true) {

            MailUser mailUser = (MailUser) opreation.rightPop("target", 10, TimeUnit.SECONDS);
            System.out.println(mailUser);
            if (mailUser == null) {
                break;
            }
            //获取邮箱发送邮件
            log.info("-------发送邮件给" + mailUser.getName() + "-----");
            try {
                mailService.regularSendGroupEmail(mailUser.getEmail());
                log.info("-------成功给" + mailUser.getName() + "发送邮件-----");
            } catch (Exception e) {
                log.info("-------给" + mailUser.getName() + "发送邮件失败-----");


            }
        }
    }

    /**
     * 给角色发送邮件   从队列中取出信息
     *
     * @throws Exception
     */
    @Scheduled (cron = "*/10 * * * * ?")
    public void sendMailByRole() throws Exception {
        int x = 0;
        ListOperations operations = redisTemplate.opsForList();
        Boolean sendTemplateMail = null;
        //是否发送模板邮件，从队列中取出
        while (x < 3) {
            //取出 sendTemplateMail（sendTemplateMail一定会有一个值）
            sendTemplateMail = (Boolean) operations.rightPop("sendTemplateMailByRole", 2, TimeUnit.SECONDS);
            if (sendTemplateMail != null) {
                break;
            }
            x++;
        }
        while (true) {
            MailRecord mailRecord = (MailRecord) operations.rightPop("mailRecord", 3, TimeUnit.SECONDS);
//            String role =(String) operations.rightPop("role",3,TimeUnit.SECONDS);
            if (mailRecord == null) {
                break;
            }
//            log.info(mailRecord + "角色是"+role);
            //获取发送邮件
            mailService.sendMailByRole(mailRecord,sendTemplateMail);
        }
    }




}
