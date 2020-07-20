package com.puhui.email.service;

import com.puhui.email.entity.Relation;

/**
 * @description: 关系增删
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RelationService {
    //添加关系
    void relationInsert(Relation relation);

    //删除关系
    void relationDelete(Relation relation);

}
