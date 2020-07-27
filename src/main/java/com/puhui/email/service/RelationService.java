package com.puhui.email.service;

import com.puhui.email.entity.MailUseRelationRoleGroup;
import com.puhui.email.entity.Relation;

/**
 * @description: 关系增删
 * @author: 杨利华
 * @date: 2020/7/9
 */
public interface RelationService {
    /**
     * //添加关系
     * @param relation
     */
    void relationInsert(Relation relation);

    /**
     * //删除关系
     * @param relation
     */
    void relationDelete(Relation relation);

    /**
     * 添加与角色组的关系
     * @param mailUseRelationRoleGroup
     */
    void roleGroupRelationAdd(MailUseRelationRoleGroup mailUseRelationRoleGroup);

    /**
     * 删除与角色组的关系
     * @param mailUseRelationRoleGroup
     */
    void roleGroupRelationDelete(MailUseRelationRoleGroup mailUseRelationRoleGroup);

}
