package com.puhui.email.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/15
 */
public class TimesUtil {
    private static Logger logger = LoggerFactory.getLogger(TimesUtil.class);
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getCurrentDate() {
        Date d = new Date();
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        System.out.println(sf.format(d));
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取当前时间，时间戳
     *
     * @return
     */
    public static long getCurrentTimesTamp() {
        String currentTime = sf.format(new Date());
        long currentTimesTamp = 0;
        try {
            Date currentDate = sf.parse(currentTime);
            currentTimesTamp = currentDate.getTime();
        } catch (ParseException e) {
            logger.error("时间戳转换异常：" + e.toString(), e);
        }
        return currentTimesTamp;
    }
}
