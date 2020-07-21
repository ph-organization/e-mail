package com.puhui.email.mapper;

import com.puhui.email.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-15:54
 */
@Mapper
public interface MessageMapper {
    void saveMessage(Message message);

    List<Message> listMessage(String sender);

    void deleteMessage(String sender);

    void deleteById(Integer id);
}
