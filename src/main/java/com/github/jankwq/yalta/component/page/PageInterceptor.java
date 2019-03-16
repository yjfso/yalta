package com.github.jankwq.yalta.component.page;

import com.github.jankwq.yalta.bean.response.PageResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yinjianfeng
 * @date 2019/1/11
 */
public class PageInterceptor implements HandlerInterceptor {

    public static final String PAGE_NAME_IN_REQUEST = "PAGE_INFO";

    private int parseInt(String s, int def){
        if (s == null){
            return def;
        }
        return Integer.valueOf(s);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod &&
                (((HandlerMethod)handler).getMethod().getReturnType() == PageResponse.class)
        ){
            int pageNum = parseInt(request.getParameter("pageNum"), 1);
            int pageSize = parseInt(request.getParameter("pageSize"), 15);
            Page page = PageHelper.startPage(pageNum, pageSize);
            request.setAttribute(PAGE_NAME_IN_REQUEST, page);
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

    }
}
