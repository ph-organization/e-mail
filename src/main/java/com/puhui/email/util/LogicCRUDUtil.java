package com.puhui.email.util;

import com.puhui.email.entity.MailUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/15
 */
@Slf4j
public class LogicCRUDUtil {

    //有效账户
    static final String SUCCEED = "true";

    //添加有效用户
    public static MailUser addSucceedUser(MailUser mailUser) {
        //true为有效用户(创建账户时默认为true)
        mailUser.setLose_user(SUCCEED);
        //创建账户时间
        String createTime = TimesUtil.getCurrentDate();
        mailUser.setCreate_time(createTime);
        return mailUser;
    }

    //判断用户是否有效（返回有效用户）
    public static Boolean succeed(String lose_user) {
        if (SUCCEED.equals(lose_user)) {
            return true;
        } else {
            return false;
        }
    }

    //判断用户是否有效（返回失效用户）
    public static Boolean failed(String lose_user) {
        if (!SUCCEED.equals(lose_user)) {
            return true;
        } else {
            return false;
        }
    }

    //返回有效用户
    public static MailUser getSucceedUser(MailUser mailUser) {
        if (mailUser.getLose_user().equals(SUCCEED)) {
            return mailUser;
        } else {
            return null;
        }
    }

    //返回有效用户集合
    public static List<MailUser> getSucceedUserS(List<MailUser> mailUser) {
        List<MailUser> mailUserList = new ArrayList<>();
        for (MailUser mus : mailUser) {
            if (mus.getLose_user().equals(SUCCEED)) {
                mailUserList.add(mus);
            } else {
                log.info(mus.getEmail() + ":用户邮箱已失效或不存在，无法查询");
            }
        }
        return mailUserList;
    }
}
