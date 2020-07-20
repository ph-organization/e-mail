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
     * @param topic    主题
     * @param content    内容
     */
    public void sendSimpleMail(MailUser user, String topic, String content, MultipartFile multipartFile) throws Exception;

    /**
     *

     */
    public void sendMailByRole(MailRecord mailRecord);


    //定时发送邮件  内容已指定
    public void regularSendGroupEmail(String to);


}
