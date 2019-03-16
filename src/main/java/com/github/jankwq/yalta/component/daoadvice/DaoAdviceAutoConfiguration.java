package com.github.jankwq.yalta.component.daoadvice;

import com.github.jankwq.yalta.component.page.PageHelperAutoConfiguration;
import com.github.jankwq.yalta.dao.daoadvice.AdviceMybatisQueryFilter;
import com.github.jankwq.yalta.dao.daoadvice.AdviceMybatisUpdateFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author yinjianfeng
 * @date 2018/12/19
 */
@Configuration
@AutoConfigureAfter({MybatisAutoConfiguration.class, PageHelperAutoConfiguration.class})
public class DaoAdviceAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired
    public DaoAdviceAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        sqlSessionFactoryList.forEach(
                sqlSessionFactory -> {
                    sqlSessionFactory.getConfiguration().addInterceptor(
                            new AdviceMybatisQueryFilter()
                    );
                    sqlSessionFactory.getConfiguration().addInterceptor(
                            new AdviceMybatisUpdateFilter()
                    );
                }
        );
    }
}
