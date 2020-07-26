package com.puhui.email.service;


import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 邹玉玺
 * @date: 2020/7/5-15:25
 */
public interface MailService {
    /**
     *发送文本邮件

     */
    public void sendSimpleMail(MailUser user, MailRecord mailRecord, Boolean sendTemplateMail) throws Exception;

    /**
     *根据角色发送邮件
     */
    public void sendMailByRole(MailRecord mailRecord,Boolean sendTemplateMail);


    //定时发送邮件  内容已指定
    public void regularSendGroupEmail(String to);


}
