package com.github.jank.yalta.dao.daoadvice;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author yinjianfeng
 * @date 2019/1/25
 */
public interface AdviceQueryDao<T> {

    String QUERY_FILTER_KEY = "advice";

    /**
     * 自动注入条件查询
     * @return
     */
    @SelectProvider(type = AdviceQueryDaoProvider.class, method = "dynamicSQL")
    List<T> adviceSelect();

}
