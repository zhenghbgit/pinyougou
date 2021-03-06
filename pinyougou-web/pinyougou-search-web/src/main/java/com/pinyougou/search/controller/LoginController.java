package com.pinyougou.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：LoginController类
 * @Date: 2018/10/31 15:06
 * @Description:登陆控制器
 * @Version：1.0
 */

@RestController
public class LoginController {

    /**
     * 获取登陆用户名
     * @param request
     * @return Map<String,String>
     */
    @GetMapping("/user/showName")
    public Map<String,String> showName(HttpServletRequest request) {

        //获取远程登陆用户名
        String loginName = request.getRemoteUser();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("loginName", loginName);
        return dataMap;

    }

}
