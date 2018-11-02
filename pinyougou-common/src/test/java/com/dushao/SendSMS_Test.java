package com.dushao;

import com.pinyougou.util.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SendSMS_Test类
 * @Date: 2018/10/26 16:30
 * @Description:短信接口调用测试
 * @Version：1.0
 */

public class SendSMS_Test {

    public static void main(String[] args) {

        //使用工具类
        HttpClientUtils httpClientUtils = new HttpClientUtils(false);

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("phoneNum", "18318646824");
        paramMap.put("signName", "五子连珠");
        paramMap.put("templateCode", "SMS_11480310");
        paramMap.put("templateParam", "{'number':'168168'}");

        //发送请求
        String s = httpClientUtils.sendPost("http://sms.pinyougou.com/sms/sendSMS", paramMap);
        System.out.println(s);

    }

}
