package com.lry.interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: CoverUtils
 * @description: 全局参数处理
 * @author: Pck
 * @create: 2023-08-08 16:51
 **/

@Component
public class GlobalParamInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 在请求到达控制器前执行的逻辑，比如对请求参数进行处理、验证或加工

        //在请求参数中添加一个全局参数
        request.setAttribute("globalParam", "This is a global parameter.");

        return true; // 返回true表示继续执行后续的拦截器和控制器，返回false表示终止请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 在控制器处理请求之后，视图渲染之前执行的逻辑

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // 在整个请求处理完成后执行的逻辑，通常用于清理资源等操作
    }
}
