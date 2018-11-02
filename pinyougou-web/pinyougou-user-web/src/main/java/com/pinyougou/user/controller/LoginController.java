package com.pinyougou.user.controller;

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
 * @Date: 2018/10/27 21:42
 * @Description:登陆控制器
 * @Version：1.0
 */

@RestController
public class LoginController {

    /**
     * 显示用户名
     * @return Map<String, String>
     */
    @GetMapping("/user/showName")
    public Map<String, String> showName() {
        //获取登陆用户名
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

        //创建Map集合，用于返回数据
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("loginName", loginName);

        return dataMap;
    }

}
