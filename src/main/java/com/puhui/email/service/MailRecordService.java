package com.puhui.email.service;

import com.puhui.email.entity.MailRecord;

import java.util.List;

/**
 * @description:
 * @author: 邹玉玺
 * @date: 2020/7/15
 */
public interface MailRecordService {
    /**
     *
     * @param mailRecord  邮件记录实体
     */
    void insertMailRecord(MailRecord mailRecord);

    /**
     *
     * @param email  邮件账号（是当前登录用户的email）
     */
    void mailRecordDelete(String email);


    /**
     *
     * @param id  邮件记录的id
     */
    void deleteMailRecordById(Integer id);

    /**
     *
     * @param email 邮件账号（是当前登录用户的email）
     * @return
     */
    List<MailRecord> mailRecordSelect(String email);


    /**
     *
     * @param email
     * @return
     */
    List<MailRecord> maillRecycleBin(String email);

    /**
     *
     * @param email
     * @return
     */
    List<MailRecord> mailRubbish(String email);


}
