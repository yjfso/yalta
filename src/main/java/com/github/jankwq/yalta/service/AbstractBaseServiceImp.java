package com.github.jankwq.yalta.service;

import com.github.jankwq.yalta.bean.BaseEntity;
import com.github.jankwq.yalta.dao.BaseDao;

import java.util.List;


public abstract class AbstractBaseServiceImp<T extends BaseEntity, D extends BaseDao<T>> implements BaseService<T> {

    protected D dao;

    protected AbstractBaseServiceImp(D dao){
        this.dao = dao;
    }

    @Override
    public void insert(T t) {
        dao.insertSelective(t);
    }

    @Override
    public List<T> selectAll() {
        return dao.selectAll();
    }

    @Override
    public T getById(Integer id) {
        return dao.selectByPrimaryKey(id);
    }

    @Override
    public void update(T t) {
        dao.updateByPrimaryKey(t);
    }

    @Override
    public List<T> adviceSelect() {
        return dao.adviceSelect();
    }

    @Override
    public List<T> adviceLeftJoinSelect() {
        return dao.adviceLeftJoinSelect();
    }
}
