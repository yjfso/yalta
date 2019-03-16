package com.github.jankwq.yalta.dao.daoadvice;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yinjianfeng
 * @date 2019/1/11
 */
public class AdviceQueryInterceptor implements HandlerInterceptor {

    public final static ThreadLocal<Example> EXAMPLE_HOLDER = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            for (Class<?> parameterType : ((HandlerMethod) handler).getMethod().getParameterTypes()) {
                if (SearchDto.class.isAssignableFrom(parameterType)) {
                    Example example = ExampleGenerater.generate(parameterType, request.getParameterMap());
                    EXAMPLE_HOLDER.set(example);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EXAMPLE_HOLDER.remove();
    }

}
