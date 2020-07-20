package com.puhui.email.util;

import com.puhui.email.entity.MailRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * @author: 邹玉玺
 * @date: 2020/7/15-19:07
 */
@Slf4j
@Component
public class EmailUtil {
    /**
     * 发送普通邮件工具类
     *
     * @param mailRecord 邮件实体类
     */
    @Value ("${mail.fromMail.sender}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;

    public MailRecord sendSimpleEmail(MailRecord mailRecord) {

        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件发送人
        message.setFrom(sender);
        //邮件接收人
        message.setTo(mailRecord.getEmail());
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
     * 发送html邮件工具类
     */
    public MailRecord sendHtmlEmail(MailRecord mailRecord) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(mailRecord.getContent(), true);
        helper.setTo(mailRecord.getEmail());
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
    public MailRecord sendMimeMessge(MailRecord mailRecord, MultipartFile multipartFile) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);//true表示支持复杂类型
        messageHelper.setFrom(sender);
        messageHelper.setTo(mailRecord.getEmail());
        messageHelper.setSubject(mailRecord.getTopic());
        messageHelper.setText(mailRecord.getContent(), true);
        messageHelper.setSentDate(new Date());

        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //文件路径
        String filepath = "D:\\upload" + File.separator + fileName;

        File file = new File(filepath);

        messageHelper.addAttachment(fileName != null ? fileName : "default.txt", file);

        //发送邮件
        String sendtime = CommonUtil.getTimeUtil();
        mailSender.send(message);
        mailRecord.setSendtime(sendtime);
        mailRecord.setFilepath(filepath);
        return mailRecord;
    }

    /**
     * 发送模板邮件
     */
    public MailRecord sendThymeleafMail(MailRecord mailRecord){
        return mailRecord;
    }
}
