package com.github.jank.yalta.service;

import com.github.jank.yalta.bean.BaseEntity;
import com.github.jank.yalta.dao.BaseDao;

import java.util.List;


public abstract class AbstractBaseServiceImp<T extends BaseEntity, D extends BaseDao<T>> implements BaseService<T> {

    protected D dao;

    protected AbstractBaseServiceImp(D dao){
        this.dao = dao;
    }


    @Override
    public void insert(T t) {
        dao.insert(t);
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
}
