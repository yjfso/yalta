package com.github.jankwq.yalta.service;

import java.util.List;

/**
 * Created by jfyin on 2018/10/2.
 */
public interface BaseService<T> {

    void insert(T t);

    List<T> selectAll();

    T getById(Integer id);

    void update(T t);

    List<T> adviceSelect();

    List<T> adviceLeftJoinSelect();
}
