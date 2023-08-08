package com.lry.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * Desc:  全局防抖
 * @param null
 * @return {@link null}
 * @author PCK
 * @date 2023/8/8 17:16
 */
@Component
public class DebounceInterceptor implements HandlerInterceptor {

    private static final long DEBOUNCE_DELAY = 1000; // 防抖延迟时间，单位毫秒
    private long lastRequestTime = 0;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime < DEBOUNCE_DELAY) {
            // 如果距离上一次请求时间小于防抖延迟时间，说明请求过于频繁，拦截请求
            return false;
        }
        lastRequestTime = currentTime;
        return true;
    }

}
