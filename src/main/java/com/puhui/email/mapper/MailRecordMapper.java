package com.puhui.email.mapper;

import com.puhui.email.entity.MailRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 邮件记录增删查
 * @author: 杨利华
 * @date: 2020/7/15
 */
@Mapper
public interface MailRecordMapper {

    void insertMailRecord(MailRecord mailRecord);

    //根据邮箱删除所有邮件记录
    void mailRecordDelete(String email);

    //根据邮箱id查询指定邮件记录
    MailRecord findMailRecordById(Integer id);

    //根据邮箱id删除指定邮件记录
    void deleteMailRecordById(Integer id);

    //根据邮箱查询邮件记录
    List<MailRecord> mailRecordSelect(String email);
}
