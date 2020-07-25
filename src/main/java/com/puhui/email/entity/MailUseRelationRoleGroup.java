package com.puhui.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @description: 邮件用户与角色组关联表
 * @author: 杨利华
 * @date: 2020/7/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "mailuser_group")
public class MailUseRelationRoleGroup {
    private Integer id;
    private Integer mailuser_id;
    private Integer role_group_id;

    public MailUseRelationRoleGroup(Integer mailuser_id, Integer role_group_id) {
        this.mailuser_id = mailuser_id;
        this.role_group_id = role_group_id;
    }
}
