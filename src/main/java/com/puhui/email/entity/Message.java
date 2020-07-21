package com.puhui.email.entity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-12:55
 */
@Data               //set get
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //无参构造
@ToString   //重写tostring方法，引入Lombok简化代码
@Table (name = "mail_message")
public class Message implements Serializable {
    @ApiParam ("id")
    private Integer id;
    @ApiParam("发件人")
    private String sender;
    @ApiParam ("内容")
    private String content;
    @ApiParam ("发送时间")
    private String sendtime;
    @ApiParam("接收人姓名")
    private String target;
    @ApiParam ("接收人电话号码")
    private String targetphone;
}
