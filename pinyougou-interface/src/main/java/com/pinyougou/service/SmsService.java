package com.pinyougou.service;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SmsService接口
 * @Date: 2018/10/26 0:43
 * @Description:短信服务接口
 * @Version：1.0
 */

public interface SmsService {

    /**
     * 发送短信
     * @param phoneNum 接收短信的手机号码
     * @param signName 短信签名
     * @param templateCode 短信模板
     * @param templateParam 短信参数（json格式）
     * @return boolean (成功true/失败false)
     */
    boolean sendSMS(String phoneNum,String signName,String templateCode,String templateParam );

}
