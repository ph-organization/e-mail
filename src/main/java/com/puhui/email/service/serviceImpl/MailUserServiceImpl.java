package com.puhui.email.service.serviceImpl;

import com.puhui.email.entity.MailUser;
import com.puhui.email.entity.MailUserList;
import com.puhui.email.entity.Relation;
import com.puhui.email.mapper.MailUserMapper;
import com.puhui.email.service.MailUserService;
import com.puhui.email.service.RelationService;
import com.puhui.email.service.RoleService;
import com.puhui.email.util.AESUtil;
import com.puhui.email.util.LogicCRUDUtil;
import com.puhui.email.util.TimesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 邮件用户crud实现
 * @author: 杨利华
 * @date: 2020/7/5
 */
@Service
@Slf4j
public class MailUserServiceImpl implements MailUserService {

    @Resource
    MailUserMapper mailUserMapper;
    @Resource
    RelationService relationService;
    @Resource
    RoleService roleService;

    /**
     * //添加单个用户
     * @param mailUser
     */
    @Override
    public void mailUserInsert(MailUser mailUser) {
        if (mailUser.getName() != null && mailUser.getEmail() != null && mailUser.getBirthday() != null && mailUser.getAddress() != null && mailUser.getPhone() != null && mailUser.getSex() != null && mailUser.getPwd() != null) {
            if (mailUser.getName().replaceAll(" +", "").equals("") || mailUser.getEmail().replaceAll(" +", "").equals("") || mailUser.getBirthday().replaceAll(" +", "").equals("") || mailUser.getAddress().replaceAll(" +", "").equals("") || mailUser.getPhone().replaceAll(" +", "").equals("") || mailUser.getSex().replaceAll(" +", "").equals("") || mailUser.getPwd().replaceAll(" +", "").equals("")) {
                log.info("请输入有效值");
            } else {
                //判断数据库中是否已有该用户(根据Email和姓名)
                if (queryUserByName(mailUser.getName()) == null && mailUserSelect(mailUser.getEmail()) == null) {
                    if (mailUser.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
                        //设置有效账号及账号创建时间
                        MailUser User = LogicCRUDUtil.addSucceedUser(mailUser);
                        //AES加密对象保存
                        MailUser AESUser = AESUtil.encryptUser(User);
                        mailUserMapper.mailUserInsert(AESUser);
                        //拿到添加用户的id
                        int mailUserId = mailUserSelect(mailUser.getEmail()).getId();
                        //拿到普通用户的id
                        int roleId = roleService.roleSelectNote("user").getId();
                        //添加用户时默认为普通用户（为添加的用户添加一个普通关联）
                        relationService.relationInsert(new Relation(mailUserId, roleId));
                    } else {
                        log.info("邮箱格式不正确");
                    }
                } else {
                    log.info(mailUser.getEmail() + "邮箱或" + mailUser.getName() + "名字已存在无法添加");
                }
            }
        } else {
            log.info("有空字符串存在，无法添加");
        }
    }

    /**
     * //根据email逻辑删除的单个用户
     * @param email
     */
    @Override
    public void mailUserLogicDelete(String email) {
        if (email == null || email.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值。");
        } else {
            //查询出该邮箱用户
            MailUser mailUser = mailUserMapper.mailUserSelect(email);
            //判断用户邮箱用户是否失效
            if (mailUser != null && LogicCRUDUtil.succeed(mailUser.getLose_user())) {
                mailUser.setLose_user("false");
                mailUserMapper.mailUserLogicDelete(mailUser);
            } else {
                log.info(email + "该用户邮箱已失效或不存在，无法删除");
            }
        }
    }

    /**
     *  //逻辑删除多个
     * @param ids
     */
    @Override
    public void mailUserLogicDeleteS(List<Integer> ids) {
        if (ids != null) {
            mailUserMapper.mailUserLogicDeleteS(ids);
        } else {
            log.info("请输入正确id");
        }
    }


    /**
     * //根据email修改单个
     * @param mailUser
     */
    @Override
    public void mailUserUpdate(MailUser mailUser) {
        if (mailUser.getName() != null && mailUser.getEmail() != null && mailUser.getBirthday() != null && mailUser.getAddress() != null && mailUser.getPhone() != null && mailUser.getSex() != null && mailUser.getPwd() != null) {
            if (mailUser.getName().replaceAll(" +", "").equals("") || mailUser.getEmail().replaceAll(" +", "").equals("") || mailUser.getBirthday().replaceAll(" +", "").equals("") || mailUser.getAddress().replaceAll(" +", "").equals("") || mailUser.getPhone().replaceAll(" +", "").equals("") || mailUser.getSex().replaceAll(" +", "").equals("") || mailUser.getPwd().replaceAll(" +", "").equals("")) {
                log.info("请输入有效值");
            } else {
                //查询出该邮箱用户
                MailUser user = mailUserMapper.mailUserSelect(mailUser.getEmail());
                //判断用户邮箱用户是否失效以及用户名字是否存在
                if (user != null && LogicCRUDUtil.succeed(user.getLose_user()) && queryUserByName(mailUser.getName()) == null) {
                    //设置修改时间
                    mailUser.setUpdate_time(TimesUtil.getCurrentDate());
                    //AES加密对象修改
                    MailUser AES_user = AESUtil.encryptUser(mailUser);
                    mailUserMapper.mailUserUpdate(AES_user);
                } else {
                    log.info(mailUser.getEmail() + "用户邮箱和已失效或不存在，无法修改");
                }
            }
        } else {
            log.info("有空字符串存在，无法修改。");
        }
    }

    /**
     * //添加逻辑删除后的用户(重启逻辑删除后的用户)
     * @param mailUser
     */
    @Override
    public void mailUserRestart(MailUser mailUser) {
        if (mailUser.getName() != null && mailUser.getEmail() != null && mailUser.getBirthday() != null && mailUser.getAddress() != null && mailUser.getPhone() != null && mailUser.getSex() != null && mailUser.getPwd() != null) {
            if (mailUser.getName().replaceAll(" +", "").equals("") || mailUser.getEmail().replaceAll(" +", "").equals("") || mailUser.getBirthday().replaceAll(" +", "").equals("") || mailUser.getAddress().replaceAll(" +", "").equals("") || mailUser.getPhone().replaceAll(" +", "").equals("") || mailUser.getSex().replaceAll(" +", "").equals("") || mailUser.getPwd().replaceAll(" +", "").equals("")) {
                log.info("请输入有效值");
            } else {
                //查询用户
                MailUser mu = mailUserFailed(mailUser.getEmail());
                //判断是否为失效用户
                if (mu != null && LogicCRUDUtil.failed(mu.getLose_user()) && queryUserByName(mailUser.getName()) == null) {
                    MailUser User = LogicCRUDUtil.addSucceedUser(mailUser);
                    //AES加密对象保存
                    MailUser AESUser = AESUtil.encryptUser(User);
                    //重启逻辑删除的用户
                    mailUserMapper.mailUserRestart(AESUser);
                    log.info("重启成功");
                } else {
                    log.info("重启失败");
                }
            }
        } else {
            log.info("有空字符串存在。请输入有效值");
        }
    }


    /**
     * //根据email查询单个(返回有效用户)
     * @param email
     * @return
     */
    @Override
    public MailUser mailUserSelect(String email) {
        if (email == null || email.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值。");
            return null;
        } else {
            //查询出该邮箱用户
            MailUser mailUser = mailUserMapper.mailUserSelect(email);
            if (mailUser != null && LogicCRUDUtil.succeed(mailUser.getLose_user())) {
                //对象返回
                return mailUser;
            } else {
                log.info(email + "该用户邮箱已失效或不存在，无法查询");
                return null;
            }
        }
    }

    /**
     * //根据email查询单个(返回失效用户)
     * @param email
     * @return
     */
    @Override
    public MailUser mailUserFailed(String email) {
        if (email == null || email.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值。");
            return null;
        } else {
            //查询出该邮箱用户
            MailUser mailUser = mailUserMapper.mailUserSelect(email);
            if (mailUser != null && LogicCRUDUtil.failed(mailUser.getLose_user())) {
                //对象返回
                return mailUser;
            } else {
                log.info(email + "该用户邮箱已失效或不存在，无法查询");
                return null;
            }
        }
    }


    /**
     * //查询全部
     * @return
     */
    @Override
    public List<MailUser> mailUserSelectAll() {
        //拿到全部用户
        List<MailUser> list = mailUserMapper.mailUserSelectAll();
        //返回有效账户
        return list.stream().filter(mailUser -> mailUser.getLose_user().equals("true")).collect(Collectors.toList());
    }

    /**
     * //根据id查询用户
     * @param ids
     * @return
     */
    @Override
    public List<MailUser> mailUserSelectById(List<Integer> ids) {
        if (ids != null) {
            //拿到id下的所有用户
            List<MailUser> list = mailUserMapper.mailUserSelectById(ids);
            //返回有效账户
            return list.stream().filter(mailUser -> mailUser.getLose_user().equals("true")).collect(Collectors.toList());
        } else {
            log.info("请输入有效值");
            return null;
        }
    }

    /**
     *  //模糊查询
     * @param email
     * @return
     */
    @Override
    public List<MailUser> mailUserDimSelect(String email) {
        if (email == null || email.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值");
            return null;
        } else {
            List<MailUser> list = mailUserMapper.mailUserDimSelect(email);
            //返回有效账户
            return list.stream().filter(mailUser -> mailUser.getLose_user().equals("true")).collect(Collectors.toList());
        }
    }


    /**
     * //根据角色查询该角色下的所有用户
     * @param role_id
     * @return
     */
    @Override
    public List<MailUser> mailUserSelectByRole(Integer role_id) {
        if (role_id == null) {
            log.info("角色不存在");
            return null;
        } else {
            List<MailUser> list = mailUserMapper.mailUserSelectByRole(role_id);
            //返回有效账户
            return list.stream().filter(mailUser -> mailUser.getLose_user().equals("true")).collect(Collectors.toList());
        }
    }

    /**
     * //根据用户姓名查询用户
     * @param name
     * @return
     */
    @Override
    public MailUser queryUserByName(String name) {
        if (name == null || name.replaceAll(" +", "").equals("")) {
            log.info("请输入有效值");
            return null;
        } else {
            MailUser mailUser = mailUserMapper.queryUserByName(name);
            //判断用户邮箱用户是否失效
            if (mailUser != null && LogicCRUDUtil.succeed(mailUser.getLose_user())) {
                //返回有效账户
                return mailUser;
            } else {
                log.info(name + "查询账户已失效或不存在");
                return null;
            }
        }
    }
}
