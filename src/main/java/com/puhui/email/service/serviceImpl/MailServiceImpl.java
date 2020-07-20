package com.puhui.email.service.serviceImpl;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.mapper.MailUserMapper;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.BaseResult;
import com.puhui.email.util.CommonUtil;
import com.puhui.email.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: 邹玉玺
 * @date: 2020/7/5-15:26
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    private static BaseResult result = new BaseResult("", "");

    @Autowired
    private MailUserService userService;
    @Resource
    private MailUserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> template;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MailRecordService mailRecordService;

    @Value ("${mail.fromMail.sender}")
    private String sender;
    @Autowired
    private EmailUtil emailUtil;

    //获取内容
    @Value ("${mail.send.content}")
    private String content;
    //获取主题
    @Value ("${mail.send.topic}")
    private String topic;

    @Override
    /**
     * 发送普通邮件接口
     */
    @Async (value = "taskExecutors")
    public void sendSimpleMail(MailUser user, String topic, String content, MultipartFile multipartFile) throws Exception {

        //获取一个随机的标识码，用于标记该用户名
        String nameCode = CommonUtil.getRandomNum();
        //获取一个随机的标识码，用于标记邮箱号
        String emailCode = CommonUtil.getRandomNum();

        //将用户名对应的缓存符放入到redis数据库   设置30分钟失效
        redisTemplate.opsForValue().set(user.getName(), nameCode, 30, TimeUnit.MINUTES);
        MailRecord mailRecord = new MailRecord();
        mailRecord.setEmail(user.getEmail());
        mailRecord.setTopic(topic);
        mailRecord.setContent(content);
            try {
                //发送邮件
                //如果有文件内容
                if (multipartFile!=null){
                    mailRecord = emailUtil.sendMimeMessge(mailRecord, multipartFile);
                }
                log.info("发送邮件给：" + mailRecord.toString());
                mailRecord = emailUtil.sendSimpleEmail(mailRecord);
                mailRecord.setResult("success");
                log.info("返回的对象是" + mailRecord);
                //获取此刻距离当天结束还剩多少秒  当做emailCode的过期时间
                Long seconds = CommonUtil.getSecondsNextEarlyMorning();
                redisTemplate.opsForValue().set(user.getEmail(), emailCode, seconds, TimeUnit.SECONDS);
                //保存邮件信息并清空邮件记录
                mailRecordService.insertMailRecord(mailRecord);
                log.info("发送成功");
            } catch (Exception e) {
                String sendtime = CommonUtil.getTimeUtil();
                mailRecord.setSendtime(sendtime);
                //发送邮件失败
                mailRecord.setResult("false");

                       //获取登录用户的email信息
                String LoginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
                //发送失败将邮件放入垃圾箱
                if (template.opsForValue().get("垃圾箱" + LoginUserEmail) != null) {
                    List<MailRecord> list = (List<MailRecord>) template.opsForValue().get("垃圾箱" + LoginUserEmail);
                    list.add(mailRecord);
                    template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
                } else {
                    List<MailRecord> list = new ArrayList<>();
                    list.add(mailRecord);
                    template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
                }

                log.error("给"+user.getName()+"发送邮件失败，已放入垃圾箱");
            }
            mailRecord = null;
    }

    /**
     * 根据角色名发送邮件给许多客户
     * （这里是群发  需要考虑并发的问题 ）
     */
    @Override
    @Async (value = "taskExecutors")
    public void sendMailByRole(MailRecord mailRecord) {
        mailRecord.setTopic(mailRecord.getTopic() + "--根据角色群发的邮件");
        try {
            //调用发送邮件工具类
            mailRecord = emailUtil.sendSimpleEmail(mailRecord);
            log.info("返回的对象是:" + mailRecord);
            //保存数据库
            mailRecord.setResult("success");
            log.info("邮件接收人" + mailRecord.getEmail() + "主题" + mailRecord.getTopic() + "内容" + mailRecord.getContent() + "邮件发送成功");
            //发送成功保存到邮件记录表
            mailRecordService.insertMailRecord(mailRecord);
        } catch (Exception e) {
            log.error("邮件接收人" + mailRecord.getEmail() + "主题" + mailRecord.getTopic() + "内容" + mailRecord.getContent() + "邮件发送出现异常");
            log.error("异常信息为" + e.getMessage());
            log.error("异常堆栈信息为-->");
            String sendtime = CommonUtil.getTimeUtil();
            mailRecord.setSendtime(sendtime);
            mailRecord.setResult("false");
            //发送失败，添加到垃圾箱

            //获取用户登录email信息
            String LoginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
            if (template.opsForValue().get("垃圾箱" + LoginUserEmail) != null) {
                List<MailRecord> list = (List<MailRecord>) template.opsForValue().get("垃圾箱" + LoginUserEmail);
                list.add(mailRecord);
                template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
            } else {
                List<MailRecord> list = new ArrayList<>();
                list.add(mailRecord);
                template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
            }
            log.error("发送失败，存入垃圾箱");
        }

    }


    /**
     * 定时发送邮件
     *
     * @param target
     */
    @Override
    @Async (value = "taskExecutors")
    public void regularSendGroupEmail(String target) {
        MailRecord mailRecord = new MailRecord();
        mailRecord.setTopic(topic + "--定时任务");
        mailRecord.setContent(content);
        mailRecord.setEmail(target);
        try {
            //发送邮件
            mailRecord = emailUtil.sendSimpleEmail(mailRecord);
            //保存数据库
            mailRecord.setResult("success");
            log.info("邮件接收人" + target + "主题" + topic + "内容" + content + "邮件发送成功");
            mailRecordService.insertMailRecord(mailRecord);
        } catch (Exception e) {
            String sendtime = CommonUtil.getTimeUtil();
            mailRecord.setSendtime(sendtime);
            mailRecord.setResult("false");

            log.error("邮件接收人" + target + "主题" + topic + "内容" + content + "邮件发送出现异常");
            log.error("异常信息为" + e.getMessage());
            log.error("异常堆栈信息为-->");
            String LoginUserEmail = (String) template.opsForValue().get("LoginUserEmail");
            if (template.opsForValue().get("垃圾箱" + LoginUserEmail) != null) {
                List<MailRecord> list = (List<MailRecord>) template.opsForValue().get("垃圾箱" + LoginUserEmail);
                list.add(mailRecord);
                template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
            } else {
                List<MailRecord> list = new ArrayList<>();
                list.add(mailRecord);
                template.opsForValue().set("垃圾箱" + LoginUserEmail, list, 14, TimeUnit.DAYS);
            }
            log.error("发送失败，存入垃圾箱");
        }

    }



}
