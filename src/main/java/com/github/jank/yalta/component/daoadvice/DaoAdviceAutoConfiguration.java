package com.github.jank.yalta.component.daoadvice;

import com.github.jank.yalta.component.page.PageHelperAutoConfiguration;
import com.github.jank.yalta.component.page.PageHelperProperties;
import com.github.jank.yalta.dao.daoadvice.AdviceMybatisQueryFilter;
import com.github.jank.yalta.dao.daoadvice.AdviceMybatisUpdateFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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
    private PageHelperProperties pageHelperProperties;

    @PostConstruct
    public void addPageInterceptor() {
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
