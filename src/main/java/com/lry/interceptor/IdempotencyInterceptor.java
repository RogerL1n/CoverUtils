package com.lry.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;


/**
 * @program: CoverUtils
 * @description:  全局接口幂等
 * @author: Pck
 * @create: 2023-08-08 17:16
 **/

@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final Set<String> processedRequests = new HashSet<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取唯一标识符
        String idempotencyKey = request.getHeader("X-Idempotency-Key");

        // 检查是否已经处理过该请求
        if (processedRequests.contains(idempotencyKey)) {
            // 已经处理过，返回错误信息或者直接返回之前处理的结果
            response.sendError(HttpServletResponse.SC_CONFLICT, "Request with the same idempotency key has been processed already.");
            return false;
        }

        // 将唯一标识符加入已处理列表，确保同一个标识符只能被处理一次
        processedRequests.add(idempotencyKey);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可在此处进行一些后处理操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求处理完成后，可以在此处清理已处理列表，以释放内存等
        String idempotencyKey = request.getHeader("X-Idempotency-Key");
        processedRequests.remove(idempotencyKey);
    }
}
