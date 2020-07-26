package com.puhui.email.manager;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: 邹玉玺
 * @date: 2020/7/24-17:06
 */
@Slf4j
@Component
public class EmailManager {

    /**
     * 发送普通邮件
     *
     * @param mailRecord 邮件实体类
     */
    @Value ("${mail.fromMail.sender}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送普通邮件
     *
     * @param mailRecord
     * @return
     */
    public MailRecord sendSimpleEmail(MailRecord mailRecord) {

        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件发送人
        message.setFrom(sender);
        //邮件接收人
        message.setTo(mailRecord.getTarget());
        //邮件主题
        message.setSubject(mailRecord.getTopic());
        //邮件内容
        message.setText(mailRecord.getContent());
        //记录发送的时间
        String sendtime = CommonUtil.getTimeUtil();
        mailRecord.setSendtime(sendtime);
        //发送邮件
        log.info("执行发送邮件");
        mailSender.send(message);
        log.info("发送完毕");
        return mailRecord;
    }

    /**
     * 发送html邮件
     */
    public MailRecord sendHtmlEmail(MailRecord mailRecord) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(mailRecord.getContent(), true);
        helper.setTo(mailRecord.getTarget());
        helper.setSubject(mailRecord.getTopic());
        helper.setFrom(sender);
        String sendtime = CommonUtil.getTimeUtil();
        mailRecord.setSendtime(sendtime);
        //发送邮件
        log.info("执行发送邮件");
        mailSender.send(message);
        log.info("发送成功");
        //记录发送的时间
        return mailRecord;
    }

    /**
     * 发送带附件的邮件
     */
    public MailRecord sendMimeMessge(MailRecord mailRecord) throws Exception {
        log.info("multipartFile" + mailRecord.getFilepath());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);//true表示支持复杂类型
        messageHelper.setFrom(sender);
        messageHelper.setTo(mailRecord.getTarget());
        messageHelper.setSubject(mailRecord.getTopic());
        messageHelper.setText(mailRecord.getContent(), true);
        messageHelper.setSentDate(new Date());

        //对文件资源进行处理

        //获取到上传后的文件
        File file = new File(mailRecord.getFilepath());
        //获取文件名
        String fileName = file.getName();
        log.info(fileName);
        messageHelper.addAttachment(fileName != null ? fileName : "default.txt", file);

        //发送邮件
        String sendtime = CommonUtil.getTimeUtil();
        mailSender.send(message);
        mailRecord.setSendtime(sendtime);
        return mailRecord;
    }

    /**
     * 发送模板邮件
     */
    public MailRecord sendTemplateMail(MailRecord mailRecord) throws MessagingException {
        //获取角色名（只有根据角色发送才不为空）
        String role = null;
        try {
            role = getRole();
        } catch (Exception e) {
            log.error("不是根据角色发送，没有角色信息");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(mailRecord.getTarget());
        helper.setSubject(mailRecord.getTopic());
        // 添加正文（使用thymeleaf模板）
        Context context = new Context();
        context.setVariable("topic", mailRecord.getTopic());
        context.setVariable("sender", sender);
        context.setVariable("content", mailRecord.getContent());
        String content = null;
        if (role != null) {
            log.info("发送邮件工具类中获取到的角色是" + role);
            log.info("该发送方式是发送给" + role + "下所有用户");
            String str1="admin";
            String str2="superAdmin";
            if (str1.equals(role) || str2.equals(role)) {
                content = this.templateEngine.process("mail/AdminTemplate", context);
            } else {
                content = this.templateEngine.process("mail/UserTemplate", context);
            }
        } else {
       log.info("不根据角色发送邮件"+mailRecord.getTarget());
            content = this.templateEngine.process("mail/UserTemplate", context);
        }
        helper.setText(content, true);
        String sendtime = CommonUtil.getTimeUtil();
        log.info("发送模板邮件");
        mailSender.send(message);
        log.info("发送成功");
        mailRecord.setSendtime(sendtime);
        return mailRecord;
    }

    /**
     * 发送带附件的模板邮件
     *
     * @param mailRecord

     * @return
     * @throws Exception
     */

    public MailRecord sendMailWithTempalteandFile(MailRecord mailRecord) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //设置发件人
        helper.setFrom(sender);
        //设置目标邮件
        helper.setTo(mailRecord.getTarget());
        //设置主题
        helper.setSubject(mailRecord.getTopic());

        //使用thymeleaf模板添加正文
        Context context = new Context();
        context.setVariable("topic", mailRecord.getTopic());
        context.setVariable("sender", sender);
        context.setVariable("content", mailRecord.getContent());
        String content = this.templateEngine.process("mail/AdminTemplate", context);
        helper.setText(content, true);

        //添加附件


        //文件路径
        String filepath = mailRecord.getFilepath();

        File file = new File(filepath);
        String fileName=file.getName();


        helper.addAttachment(fileName != null ? fileName : "default.txt", file);

        //发送邮件
        log.info("发送带附件的模板邮件");
        mailSender.send(message);
        String sendtime = CommonUtil.getTimeUtil();
        log.info("发送成功");
        mailRecord.setSendtime(sendtime);
        return mailRecord;
    }

    /**
     * 获取是否是发送邮件给某一角色
     *
     * @return
     */
    public String getRole() {
        ListOperations options = redisTemplate.opsForList();
        String role = null;
        int x=0;
        while (x<5) {
            role = (String) options.rightPop("role",3, TimeUnit.SECONDS);
            if (role != null) {
                break;
            }
            x++;
        }
        return role;
    }

}
