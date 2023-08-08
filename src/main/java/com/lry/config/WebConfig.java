package com.lry.config;

import com.lry.interceptor.DebounceInterceptor;
import com.lry.interceptor.GlobalParamInterceptor;
import com.lry.interceptor.IdempotencyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalParamInterceptor globalParamInterceptor;


    @Autowired
    private DebounceInterceptor debounceInterceptor;

    @Autowired
    private  IdempotencyInterceptor idempotencyInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalParamInterceptor);
        registry.addInterceptor(debounceInterceptor);
        registry.addInterceptor(idempotencyInterceptor).addPathPatterns("/**");
    }
}
