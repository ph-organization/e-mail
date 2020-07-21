package com.puhui.email.util;

import com.puhui.email.entity.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-15:39
 */
@Slf4j
public class MessageUtil {
    public static void sendMessage(Message message){
           log.info("发送短信中-------------");

           log.info("短信发送完毕");
    }
}
