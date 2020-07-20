package com.puhui.email.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/9
 */
public class MailUserList implements Serializable {

    private List<MailUser> mailUserList;

    public List<MailUser> getMailUserList() {
        return mailUserList;
    }

    public void setMailUserList(List<MailUser> mailUserList) {
        this.mailUserList = mailUserList;
    }
}
