package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：OrderController类
 * @Date: 2018/11/1 14:07
 * @Description:订单控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("order")
public class OrderController {

    //引入订单服务
    @Reference(timeout = 10000)
    private OrderService orderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;

    /**
     * 保存订单
     * @param order
     * @param request
     * @return boolean
     */
    @PostMapping("save")
    public boolean saveOrder(@RequestBody Order order, HttpServletRequest request) {

        try {

            //获取登陆用户名
            String loginName = request.getRemoteUser();
            //封装用户名到订单对象
            order.setUserId(loginName);
            //设置订单来源PC端：2
            order.setSourceType("2");
            //保存订单信息
            orderService.save(order);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 生成微信支付二维码
     * @return Map<String,String>
     */
    @GetMapping("genPayCode")
    public Map<String,String> genPayCode(HttpServletRequest request){

        //获取登陆用户名
        String loginName = request.getRemoteUser();

        //从Redis中获取日志对象
        PayLog payLog = orderService.findPayLogFormRedis(loginName);

        //生成微信支付二维码，返回
        return weixinPayService.genPayCode(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
    }

    /**
     * 根据支付订单号查询微信支付订单的状态
     * @return Map<String,String>
     */
    @GetMapping("queryPayStatus")
    public Map<String,Integer> queryPayStatus(String outTradeNo) {

        //根据支付订单号查询订单
        Map<String, String> map = weixinPayService.queryPayStatus(outTradeNo);

        //创建Map集合,用于封装返回数据
        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("status", 3);

        //判断状态码是否成功
        if (map != null && map.size() > 0 && map.get("return_code").equals("SUCCESS")) {

            //判断支付状态
            if (map.get("trade_state").equals("SUCCESS")) {
                //当用户支付成功后，修改日志与订单状态
                orderService.updateOrderStatus(outTradeNo,map.get("transaction_id"));
                dataMap.put("status", 1);
            }
            if(map.get("trade_state").equals("NOTPAY")){

                dataMap.put("status", 2);
            }

        }

        return dataMap;
    }

}
