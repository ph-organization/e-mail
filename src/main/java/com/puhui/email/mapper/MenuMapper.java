package com.puhui.email.mapper;

import com.puhui.email.entity.Menu;

import java.util.List;

/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/21
 */
public interface MenuMapper {
    /**
     * 拿到菜单全部信息
     * @return
     */
    List<Menu> getAllMenu();
}
