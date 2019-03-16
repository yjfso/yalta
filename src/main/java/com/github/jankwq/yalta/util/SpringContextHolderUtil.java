package com.github.jankwq.yalta.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yinjianfeng
 * @date 2018/11/13
 */
public class SpringContextHolderUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolderUtil.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        assert applicationContext != null;
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> type){
        assert applicationContext != null;
        return applicationContext.getBean(type);
    }
}
