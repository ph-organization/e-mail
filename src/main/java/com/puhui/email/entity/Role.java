package com.puhui.email.entity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @description: 角色实体（用户权限）
 * @author: 杨利华
 * @date: 2020/7/7
 */
@Data   //相当于加入getset方法
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //
@ToString   //重写tostring方法，引入Lombok简化代码
@Table(name = "t_role")
public class Role {
    @ApiParam("角色id(不填)")
    private Integer id;
    @ApiParam("角色名")
    private String role_name;
    @ApiParam("角色权限")
    private String role_admin;

    public Role(String role_admin, String role_name) {
        this.role_admin = role_admin;
        this.role_name = role_name;
    }

}
