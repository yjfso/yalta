package com.github.jank.yalta.dao.daoadvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yinjianfeng
 * @date 2019/1/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QueryMeta {

    Class value();

    String[] equals() default {};

    String[] like() default {};

    String[] lt() default {};

    String[] gt() default {};

    String[] order() default {};

}
