package com.puhui.email.service;

import com.puhui.email.entity.Message;

import java.util.List;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-14:57
 */
public interface MessageService {

    public void sendMessage(Message message);

    List<Message> listMessage();

    void deleteMessage();

    void deleteById(Integer id);

}
