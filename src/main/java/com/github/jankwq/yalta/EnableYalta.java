package com.github.jankwq.yalta;

import com.github.jankwq.yalta.component.daoadvice.EnableDaoAdvice;
import com.github.jankwq.yalta.component.error.EnableErrorHandler;
import com.github.jankwq.yalta.component.page.EnablePageInterceptor;

import java.lang.annotation.*;

/**
 * @author yinjianfeng
 * @date 2019/2/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@EnablePageInterceptor
@EnableDaoAdvice
@EnableErrorHandler
public @interface EnableYalta {
}
