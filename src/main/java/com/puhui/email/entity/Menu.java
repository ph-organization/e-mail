package com.puhui.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.util.List;

/**
 * @description: 资源实体
 * @author: 杨利华
 * @date: 2020/7/21
 */
@Data   //相当于加入getset方法
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor  //无参构造
@ToString   //重写tostring方法，引入Lombok简化代码
@Table(name = "menu")
public class Menu {
    private Integer id;
    private String pattern;
    private List<Role> roles;
}
