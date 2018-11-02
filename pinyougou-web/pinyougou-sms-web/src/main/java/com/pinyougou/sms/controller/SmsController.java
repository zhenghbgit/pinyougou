package com.pinyougou.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SmsController类
 * @Date: 2018/10/26 0:59
 * @Description:短信服务接口
 * @Version：1.0
 */

@RestController
@RequestMapping("/sms")
public class SmsController {

    //引用短信服务
    @Reference(timeout = 10000)
    private SmsService smsService;

    @PostMapping("/sendSMS")
    public Map<String, Object> sendSMS(String phoneNum, String signName,
                                       String templateCode, String templateParam) {

        //发送短信验证码
        boolean success = smsService.sendSMS(phoneNum, signName, templateCode, templateParam);

        //创建map集合，用于储存短信发送的成功/失败状态
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);

        return map;

    }

}
