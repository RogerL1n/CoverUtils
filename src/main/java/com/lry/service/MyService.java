package com.lry.service;

import com.lry.annotation.Debounce;
import org.springframework.stereotype.Service;

/**
 * @program: CoverUtils
 * @description:
 * @author: Pck
 * @create: 2023-08-08 17:13
 **/

@Service
public class MyService {

    @Debounce
    public void debounceMethod() {
        // 在这里实现防抖方法的逻辑
    }
}
