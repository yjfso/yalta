package com.github.jankwq.yalta.component.error;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yinjianfeng
 * @date 2019/2/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ErrorHandler.class})
public @interface EnableErrorHandler {
}
