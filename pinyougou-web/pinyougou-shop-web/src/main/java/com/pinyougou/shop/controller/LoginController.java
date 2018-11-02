package com.pinyougou.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：LoginController类
 * @Date: 2018/10/10 2:00
 * @Description:登陆相关控制器
 * @Version：1.0
 */

@RestController
public class LoginController {

    /**
     * 获取登陆用户名
     * @return Map<String,String>
     */
    @GetMapping("/showLoginName")
    public Map<String,String> showLoginName(){

        //获取security域对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //从security域对象中获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //从认证对象中获取用户名
        String loginName = authentication.getName();

        //把数据封装到map集合
        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);

        return map;
    }

}
