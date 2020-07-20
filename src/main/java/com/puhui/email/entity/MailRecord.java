package com.puhui.email.entity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @description: 邮件记录
 * @author: 杨利华
 * @date: 2020/7/15
 */
@Data               //set get
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //无参构造
@ToString   //重写tostring方法，引入Lombok简化代码
@Table(name = "mail_record")
public class MailRecord {

    @ApiParam("id")
    private Integer id;

    @ApiParam("邮箱")
    private String email;
    //发送结果
    @ApiParam("发送邮件是否成功")
    private String result;
    //邮件内容
    @ApiParam("邮件内容")
    private String content;
    //邮件主题
    @ApiParam("邮件主题")
    private String topic;

    @ApiParam("发送的时间")
    private String sendtime;

    @ApiParam ("附件地址")
    private String filepath;

    public MailRecord( Integer id) {
        this.id=id;
    }

    public MailRecord(String email, String content, String topic, String result, String sendtime) {
        this.email = email;
        this.result = result;
        this.content = content;
        this.topic = topic;
        this.sendtime = sendtime;
    }

    //用于删除指定邮箱和指定主题的邮件记录
    public MailRecord(String email, String topic) {
        this.email = email;
        this.topic = topic;
    }
}
