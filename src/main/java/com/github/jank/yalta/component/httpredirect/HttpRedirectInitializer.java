package com.github.jank.yalta.component.httpredirect;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author yinjianfeng
 * @date 2019/2/25
 */
public class HttpRedirectInitializer implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }
}
