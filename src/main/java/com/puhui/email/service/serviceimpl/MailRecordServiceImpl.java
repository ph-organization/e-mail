package com.puhui.email.service.serviceimpl;

import com.puhui.email.entity.MailRecord;
import com.puhui.email.entity.MailUser;
import com.puhui.email.mapper.MailRecordMapper;
import com.puhui.email.service.MailRecordService;
import com.puhui.email.service.MailUserService;
import com.puhui.email.util.LogicCRUDUtil;
import com.puhui.email.util.SecurityGetName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/15
 */
@Service
@Slf4j
public class MailRecordServiceImpl implements MailRecordService {
    @Resource
    MailRecordMapper mailRecordMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    MailUserService mailUserService;

    @Override
    public void insertMailRecord(MailRecord mailRecord) {
                //添加记录
                mailRecordMapper.insertMailRecord(mailRecord);
    }

    //根据邮箱删除邮件记录（邮件记录没有做逻辑删除）
    @Override
    public void mailRecordDelete(String email) {
        if (email == null || "".equals(email.replaceAll(" +", ""))) {
            log.info("没有该用户邮件记录。");
        } else {
            //删除之前先放到redis缓存,7天
            List<MailRecord> mailRecords = mailRecordMapper.mailRecordSelect(email);
            if (mailRecords!=null) {
                //查看redis是否已存在该用户回收站
                if (redisTemplate.opsForValue().get("email回收站" + email) != null) {
                    //回收站已存在，先取出集合并追加进去
                    List<MailRecord> list = (List<MailRecord>) redisTemplate.opsForValue().get("回收站" + email);
                    //将邮件记录追加进去
                    List<MailRecord> addlist = list.stream().sequential().collect(Collectors.toCollection(() -> mailRecords));

                    //list存入redis
                    redisTemplate.opsForValue().set("email回收站" + email, addlist, 7, TimeUnit.DAYS);
                    log.info("追加到已有回收站成功");
                } else {
                    //直接存入缓存
                    redisTemplate.opsForValue().set("email回收站" + email, mailRecords, 7, TimeUnit.DAYS);
                    log.info("添加到回收站成功");
                }
                //进行删除
                mailRecordMapper.mailRecordDelete(email);
                log.info("删除成功");
            }else {
                log.info("暂时没有邮件记录");
            }
        }

    }



    //根据邮箱查询邮件记录
    @Override
    public List<MailRecord> mailRecordSelect(String email) {
        List<MailRecord> mailRecordList = null;
        if (email == null || "".equals(email.replaceAll(" +", ""))) {
            log.info("请输入有效值");
            return null;
        } else {
            MailUser mailUser = mailUserService.mailUserSelect(email);
            //判断用户邮箱是否有效
            if (mailUser != null && LogicCRUDUtil.succeed(mailUser.getLose_user())) {
                mailRecordList = mailRecordMapper.mailRecordSelect(email);
                return mailRecordList;
            } else {
                return null;
            }
        }
    }


    //根据邮箱查询7天内已删除的邮件记录   (数据在redis缓存中，key是"回收站"+email)
    @Override
    public List<MailRecord> maillRecycleBin(String email) {
        List<MailRecord> list = (List<MailRecord>) redisTemplate.opsForValue().get("email回收站"+email);
        return list;
    }

    @Override
    public List<MailRecord> mailRubbish(String email) {
        //返回缓存中发送失败的垃圾邮件
        List<MailRecord> mailRecords =(List<MailRecord>) redisTemplate.opsForValue().get("email垃圾箱" + email);
        return mailRecords;
    }

    //根据邮件记录id删除指定记录
    @Override
    public void deleteMailRecordById(Integer id) {
        MailRecord mailRecordById = mailRecordMapper.findMailRecordById(id);
        String email= SecurityGetName.getUsername();
        log.info("当前用户名"+email);
        //删除之前先放到redis缓存
        if(redisTemplate.opsForValue().get("email回收站"+email)!=null){
            //回收站已存在，先取出集合并追加进去
            List<MailRecord> list = (List<MailRecord>) redisTemplate.opsForValue().get("email回收站"+email);
            //向list追加需要删除的邮件记录
            list.add(mailRecordById);
            //放入缓存
            redisTemplate.opsForValue().set("email回收站"+email,list,7, TimeUnit.DAYS);
        }else {
            List<MailRecord> mailRecords=new ArrayList<>();
            mailRecords.add(mailRecordById);
            //放入缓存
            redisTemplate.opsForValue().set("email回收站"+email,mailRecords,7, TimeUnit.DAYS);
        }

        //进行删除
        mailRecordMapper.deleteMailRecordById(id);
    }
}
