package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.service.WeixinPayService;
import com.pinyougou.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：WeixinPayServiceImpl类
 * @Date: 2018/11/1 15:52
 * @Description:微信支付服务接口
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.WeixinPayService")
public class WeixinPayServiceImpl implements WeixinPayService {

    //注入微信公众号
    @Value("${wx.appid}")
    private String appid;

    //注入商户账号
    @Value("${wx.partner}")
    private String partner;

    //注入商户密钥
    @Value("${wx.partnerkey}")
    private String partnerkey;

    //统一下单请求地址
    @Value("${unifiedorder}")
    private String unifiedorder;

    //订单查询地址
    @Value("${orderquery}")
    private String orderquery;

    /**
     * 生成微信支付二维码
     * @param outTradeNo 支付订单号
     * @param totalFee  支付金额
     * @return Map<String , String>
     */
    @Override
    public Map<String, String> genPayCode(String outTradeNo, String totalFee) {

        //创建Map集合，用于封装请求参数
        Map<String, String> param = new HashMap<>();
        //封装公众号ID
        param.put("appid", appid);
        //封装商户号
        param.put("mch_id", partner);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        param.put("body", "品优购");
        //商户订单号
        param.put("out_trade_no", outTradeNo);
        //总金额（分）
        param.put("total_fee", totalFee);
        //终端IP
        param.put("spbill_create_ip", "127.0.0.1");
        //通知地址[回调地址(随意写)]
        param.put("notify_url", "http://test.itcast.cn");
        //交易类型
        param.put("trade_type", "NATIVE");

        try {

            //根据商户密钥签名生成XML格式请求参数
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求参数：" + xml);

            //创建httpClienUtils请求对象
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            //发送请求,得到响应数据
            String result = httpClientUtils.sendPost(unifiedorder, xml);
            System.out.println("响应数据：" + result);
            //将响应数据利用微信工具类转化为Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            //获取响应状态码
            String returnCode = resultMap.get("return_code");

            //创建Map集合，用于封装返回信息
            Map<String, String> map = new HashMap<>();
            //封装总金额
            map.put("totalFee", totalFee);
            //封装支付订单号
            map.put("outTradeNo", outTradeNo);

            //获取返回信息（错误原因）
            String return_msg = resultMap.get("return_msg");
            System.out.println("错误信息：" + return_msg);

            //如果响应状态码为成功，就获取二维码链接
            if (returnCode.equals("SUCCESS")) {
                //获取二维码链接
                String codeUrl = resultMap.get("code_url");
                //封装二维码
                map.put("codeUrl", codeUrl);

                return map;
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取返回微信的支付订单状态
     * @param outTradeNo
     */
    @Override
    public Map<String, String> queryPayStatus(String outTradeNo) {

        //封装请求参数
        Map<String, String> param = new HashMap<>();
        //封装公众账号
        param.put("appid", appid);
        //封装商户号
        param.put("mch_id", partner);
        //封装商户订单号
        param.put("out_trade_no", outTradeNo);
        //疯转随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());

        try {
            //根据商户密钥签名生成xml格式请求参数
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);

            //创建httpClient请求对象
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            //发送请求,获得响应数据
            String result = httpClientUtils.sendPost(orderquery, xml);

            //使用微信工具类转化为map
            Map<String, String> map = WXPayUtil.xmlToMap(result);



            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
