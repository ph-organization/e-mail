package com.puhui.email.task;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.mapper.MailRecordMapper;
import com.puhui.email.mapper.MailUserMapper;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.util.LogicCRUDUtil;
import com.puhui.email.util.TimesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/9
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class SaticScheduleTask {

    @Resource
    MailUserService mailUserService;
    @Resource
    MailRecordService mailRecordService;
    @Autowired
    private MailRecordService recordService;
    @Autowired
    MailUserMapper userMapper;
    @Autowired
    MailRecordMapper recordMapper;

    //添加定时任务
    @Scheduled(cron = "0 */30 * * * ?")
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        for (MailUser mailUser : mailUserService.mailUserSelectAll()) {
            log.info("执行静态定时查询任务: " + mailUser);
        }
    }
/**
 * 杨利华
 */
//    //添加定时删除邮件记录任务
//    @Scheduled(cron = "0 */30 * * * ?")
//    //@Scheduled(fixedRate=5000)
//    private void delete() {
//        //循环删除无效用户邮件记录
//        for (MailUser mailUser : mailUserService.mailUserSelectAll()) {
//            //判断无效用户
//            if (!LogicCRUDUtil.succeed(mailUser.getLose_user())) {
//                mailRecordService.mailRecordDelete(mailUser.getEmail());
//                log.info("执行静态定时删除任务:删除无效用户邮件记录,删除成功。");
//            } else {
//                log.info("没有无效用户，不执行删除。");
//            }
//        }
//        //获取当前时间戳
//        Long today = TimesUtil.getCurrentTimesTamp();
//        //一天的时间
//        long nd = 1000 * 24 * 60 * 60;
//        //循环删除时间超过一周的邮件记录
//        for (MailRecord mailRecord : mailRecordService.mailRecordSelectAll()) {
//            //将邮件发送时间转换为时间戳
//            Long oldDay = TimesUtil.getStringToDate(mailRecord.getSend_time());
//            int interval = (int) ((today - oldDay) / nd);
//            if (interval >= 7) {
//                mailRecordService.mailRecordDelete(mailRecord.getEmail());
//                log.info("执行静态定时删除任务:删除无效用户邮件记录,删除成功。");
//            } else {
//                log.info("没有时间超过一周的邮件记录，不执行删除。");
//            }
//        }
//    }

    /**
     * 邹玉玺
     * 定时执行清空已过期用户邮件记录，清空之前先放入redis缓存一周后清空
     * 每天凌晨0点执行一次清空
     */
    @Scheduled(cron = "0 0 */23 * * ?")
    // @Scheduled(cron = "0 */1 * * * ?")
    public void  deleteMailRecordOntime() {
        List<MailUser> users = userMapper.mailUserSelectAll();
        // 1 查询已过期用户(一个list集合)   获取其 email

        if (users!=null) {
            //将已过期用户取出来放入List集合中
            List<MailUser> list = users.stream().filter(str -> "false".equals(str.getLose_user())).collect(Collectors.toList());
            //如果List不为空
            if (list!= null) {
                //2 根据已过期用户email清理邮件记录
                list.forEach(str->{
                    //根据email查询邮件记录
                    List<MailRecord> mailRecords = recordMapper.mailRecordSelect(str.getEmail());
                    //根据已过期用户邮箱删除邮件记录
                    log.info("清理已过期用户"+ str.getName()+"对应的邮件记录");
                    recordService.mailRecordDelete(str.getEmail());
                });
                log.info("清理邮件记录完毕");
            }
        }
    }
}
