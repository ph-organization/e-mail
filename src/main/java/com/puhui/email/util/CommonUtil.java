package com.puhui.email.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class CommonUtil {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式

    /**
     * 以UUID重命名
     *
     * @param fileName
     * @return
     */
    public static String renamePic(String fileName) {
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extName;
    }

    /**
     * 随机6位数生成
     */
    public static String getRandomNum() {

        Random random = new Random();
        int num = random.nextInt(999999);
        //不足六位前面补0
        String str = String.format("%06d", num);
        return str;
    }

    /**
     * 批量递归删除时 判断target是否在ids中 避免重复删除
     *
     * @param target
     * @param ids
     * @return
     */

    public static Boolean judgeIds(String target, String[] ids) {

        Boolean flag = false;
        for (String id : ids) {
            if (id.equals(target)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 计算此刻距离凌晨的秒数
     *
     * @return
     */

    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        //设置时间是这一年中加一天
        cal.add(Calendar.DAY_OF_YEAR, 1);
        //设置加的这一天的时间是0点
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //设置加的这一天的时间是0秒
        cal.set(Calendar.SECOND, 0);
        //设置加的这一天的时间是0分钟
        cal.set(Calendar.MINUTE, 0);
        //设置加的这一天的时间是0毫秒
        cal.set(Calendar.MILLISECOND, 0);
        //当前时间到设置的这一天0点的时间还剩多少秒
        long seconds = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        return seconds;
    }

    public static String getTimeUtil() {
        String date = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }

    public static void main(String[] args) {
        String timeUtil = getTimeUtil();
        System.out.println(timeUtil);
    }
}