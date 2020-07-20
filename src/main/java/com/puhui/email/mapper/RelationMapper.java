package com.puhui.email.mapper;

import com.puhui.email.entity.Relation;

/**
 * @description: 用户与角色
 * @author: 杨利华
 * @date: 2020/7/9
 */

public interface RelationMapper {
    //添加关系
    void relationInsert(Relation relation);

    //删除关系
    void relationDelete(Relation relation);

}
