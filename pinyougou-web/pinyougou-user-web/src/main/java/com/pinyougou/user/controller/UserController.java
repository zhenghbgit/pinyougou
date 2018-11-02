package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UserController类
 * @Date: 2018/10/26 17:42
 * @Description:用户控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("user")
public class UserController {

    //引入用户服务
    @Reference(timeout = 10000)
    private UserService userService;

    /**
     * 保存用户信息
     * @param user
     * @return boolean
     */
    @PostMapping("save")
    public boolean saveUser(@RequestBody User user,String code) {

        try {
            //判断验证码是否为空
            if (StringUtils.isNoneBlank(code)) {
                //判断验证码是否正确
                boolean b = userService.checkSMSCode(user.getPhone(), code);
                if (b){
                    userService.save(user);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 注册用户发送短信验证码
     * @param phoneNum
     * @return boolean
     */
    @GetMapping("sendSMS")
    public boolean sendSMS(String phoneNum) {

        try {
            //判断手机号码是否为空
            if (StringUtils.isNoneBlank(phoneNum)) {
                //发送短信验证码
                userService.sendSMS(phoneNum);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
