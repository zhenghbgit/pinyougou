package com.pinyougou.service;

import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：WeixinPayService接口
 * @Date: 2018/11/1 15:53
 * @Description:微信服务接口
 * @Version：1.0
 */

public interface WeixinPayService {

    /**
     * 生成微信支付二维码
     * @param outTradeNo 支付订单号
     * @param totalFee  支付金额
     * @return Map<String, String>
     */
    Map<String, String> genPayCode(String outTradeNo,String totalFee);

    /**
     * 获取返回微信的支付订单状态
     * @param outTradeNo
     * @return Map<String,String>
     */
    Map<String,String> queryPayStatus(String outTradeNo);
}
