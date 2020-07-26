package com.puhui.email.service;

import com.puhui.email.entity.MailRecord;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/15
 */
public interface MailRecordService {

    void insertMailRecord(MailRecord mailRecord);

    //根据邮箱删除邮件记录
    void mailRecordDelete(String email);

    //根据id删除指定邮件记录
    void deleteMailRecordById(Integer id);
    //根据邮箱查询邮件记录
    List<MailRecord> mailRecordSelect(String email);


    //根据邮箱查询7天内已删除的邮件记录
    List<MailRecord> maillRecycleBin(String email);

    //查看垃圾箱里的邮件
    List<MailRecord> mailRubbish(String email);


}
