package com.github.jankwq.yalta.component.httpredirect;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yinjianfeng
 * @date 2019/2/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HttpRedirectInitializer.class)
public @interface EnableHttpRedirect {
}
