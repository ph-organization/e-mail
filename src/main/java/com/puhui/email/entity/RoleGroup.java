package com.puhui.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @description: 角色组
 * @author: 杨利华
 * @date: 2020/7/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "role_group")
public class RoleGroup {
    private Integer id;
    private String group_name;
}
