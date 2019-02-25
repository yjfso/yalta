package com.github.jank.yalta.component.daoadvice;

import com.github.jank.yalta.component.page.EnablePageInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yinjianfeng
 * @date 2019/2/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@EnablePageInterceptor
@Import({DaoAdviceAutoConfiguration.class, DaoAdviceInterceptorConfiguration.class})
public @interface EnableDaoAdvice {
}
