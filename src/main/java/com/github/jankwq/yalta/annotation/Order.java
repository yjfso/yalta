package com.github.jankwq.yalta.annotation;

import tk.mybatis.mapper.entity.Example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yinjianfeng
 * @date 2019/3/6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {

    String value();

    enum OrderType {
        //
        None,
        asc {

            @Override
            public void order(Example.OrderBy orderBy) {
                orderBy.asc();
            }
        },
        desc {

            @Override
            public void order(Example.OrderBy orderBy) {
                orderBy.desc();
            }
        };

        public void order(Example.OrderBy orderBy) {};
    }

    OrderType type() default OrderType.None;
}
