package com.lry.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: CoverUtils
 * @description:
 * @author: Pck
 * @create: 2023-08-08 16:54
 **/

@RestController
public class TestController {


    @GetMapping("/global/api")
    public String yourApiMethod(HttpServletRequest request) {
        String globalParam = (String) request.getAttribute("globalParam");
        // 处理全局参数逻辑
        return "Your API response with global parameter: " + globalParam;
    }


}
