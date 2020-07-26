package com.puhui.email.service.serviceimpl;

import com.puhui.email.entity.Relation;
import com.puhui.email.mapper.RelationMapper;
import com.puhui.email.service.RelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 关系增删
 * @author: 杨利华
 * @date: 2020/7/9
 */
@Service
@Slf4j
public class RelationServiceImpl implements RelationService {

    @Resource
    RelationMapper relationMapper;

    //添加用户与角色的关联
    @Override
    public void relationInsert(Relation relation) {
        relationMapper.relationInsert(relation);
    }

    //删除关联
    @Override
    public void relationDelete(Relation relation) {
        relationMapper.relationDelete(relation);
    }
}
