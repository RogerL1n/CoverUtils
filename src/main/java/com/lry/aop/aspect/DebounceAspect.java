package com.lry.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:  局部防抖 基于注解
 * @param null
 * @return {@link null}
 * @author PCK
 * @date 2023/8/8 17:14
 */

@Aspect
@Component
public class DebounceAspect {
    private Map<String, Long> lastInvocationMap = new HashMap<>();
    private final long debounceInterval = 1000; // 防抖时间间隔，单位毫秒

    @Before("@annotation(Debounce)")
    public void debounceAdvice() throws Throwable {
        // 获取方法名
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        // 获取上次调用时间
        long lastInvocationTime = lastInvocationMap.getOrDefault(methodName, 0L);

        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        // 判断是否在防抖间隔内
        if (currentTime - lastInvocationTime >= debounceInterval) {
            // 更新上次调用时间
            lastInvocationMap.put(methodName, currentTime);
        } else {
            // 在防抖间隔内，取消当前方法的调用
            throw new RuntimeException("Debounced");
        }
    }
}
