package com.github.jank.yalta.dao;

import com.github.jank.yalta.bean.BaseEntity;
import com.github.jank.yalta.dao.daoadvice.AdviceQueryDao;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Description
 * <p>
 * </p>
 * DATE 2018/1/2.
 *
 * @author liweijian.
 */
public interface BaseDao<T extends BaseEntity> extends MySqlMapper<T>, Mapper<T>, IdsMapper<T>, AdviceQueryDao<T> {
}