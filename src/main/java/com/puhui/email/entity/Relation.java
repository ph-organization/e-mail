package com.puhui.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @description: 角色 用户联系表
 * @author: 杨利华
 * @date: 2020/7/8
 */

@Data   //相当于加入getset方法
//@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //
@ToString   //重写tostring方法，引入Lombok简化代码
@Table(name = "t_realtion")
public class Relation {
    private Integer id;
    private Integer role_id;
    private Integer user_id;

    public Relation(Integer user_id, Integer role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }
}
