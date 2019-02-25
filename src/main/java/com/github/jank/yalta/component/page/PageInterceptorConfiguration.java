package com.github.jank.yalta.component.page;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yinjianfeng
 * @date 2019/2/22
 */
@Configuration
public class PageInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageInterceptor())
                .addPathPatterns("/**");
    }

}
