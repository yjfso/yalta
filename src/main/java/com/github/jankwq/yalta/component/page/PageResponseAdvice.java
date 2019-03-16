package com.github.jankwq.yalta.component.page;

import com.github.jankwq.yalta.bean.response.PageResponse;
import com.github.pagehelper.Page;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yinjianfeng
 * @date 2019/1/14
 */
@ControllerAdvice
public class PageResponseAdvice implements ResponseBodyAdvice<PageResponse> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getReturnType() == PageResponse.class;
    }

    @Override
    public PageResponse beforeBodyWrite(PageResponse body,
                                        MethodParameter returnType, MediaType selectedContentType,
                                        Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Page page = (Page) req.getAttribute(PageInterceptor.PAGE_NAME_IN_REQUEST);
        if (page != null){
            body.setPage(page.getPageNum(), page.getPageSize());
            body.setTotalCount((int)page.getTotal());
            body.setTotalPage(page.getPages());
        }
        return body;
    }

}
