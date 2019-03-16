package com.github.jankwq.yalta.component.daoadvice;

import com.github.jankwq.yalta.dao.daoadvice.AdviceQueryInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yinjianfeng
 * @date 2019/2/22
 */
@Configuration
public class DaoAdviceInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdviceQueryInterceptor())
                .addPathPatterns("/**");
    }

}
