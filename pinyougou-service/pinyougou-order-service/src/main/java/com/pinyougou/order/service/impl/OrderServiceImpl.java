package com.pinyougou.order.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.Cart;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：OrderServiceImpl类
 * @Date: 2018/11/1 14:13
 * @Description:订单服务接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.OrderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    //注入订单数据访问层
    @Autowired
    private OrderMapper orderMapper;
    //注入订单商品明细数据访问层
    @Autowired
    private OrderItemMapper orderItemMapper;
    //注入redis
    @Autowired
    private RedisTemplate redisTemplate;
    //注入分布式ID生成类
    @Autowired
    private IdWorker idWorker;
    //注入支付日志数据访问层
    @Autowired
    private PayLogMapper payLogMapper;

    /**
     * 添加方法
     *
     * @param order
     */
    @Override
    public void save(Order order) {

        try {

            //根据用户名从Redis中获取用户的购物车数据
            List<Cart> carts = (List<Cart>) redisTemplate.boundValueOps("cart_" + order.getUserId()).get();

            //定义订单号集合
            List<String> orderList = new ArrayList<>();
            //定义多个订单支付的总金额（元）
            double totalMoney = 0;

            //遍历购物车
            for (Cart cart : carts) {
                //一个商家购物车，一个订单
                //创建商家订单
                Order shopOrder = new Order();
                //使用分布式id生成器生成id
                long orderId = idWorker.nextId();
                //订单id
                shopOrder.setOrderId(orderId);
                //支付类型
                shopOrder.setPaymentType(order.getPaymentType());
                //状态:1 未付款
                shopOrder.setStatus("1");
                //订单创建时间
                shopOrder.setCreateTime(new Date());
                //订单修改时间
                shopOrder.setUpdateTime(shopOrder.getCreateTime());
                //购买的用户名
                shopOrder.setUserId(order.getUserId());
                //收货地址
                shopOrder.setReceiverAreaName(order.getReceiverAreaName());
                //收货手机
                shopOrder.setReceiverMobile(order.getReceiverMobile());
                //收件人
                shopOrder.setReceiver(order.getReceiver());
                //订单来源
                shopOrder.setSourceType(order.getSourceType());
                //商家id
                shopOrder.setSellerId(cart.getSellerId());

                //定义该订单的总金额
                double money = 0;

                //遍历该订单的商品明细
                for (OrderItem orderItem : cart.getOrderItems()) {

                    //设置主键
                    orderItem.setId(idWorker.nextId());
                    //设置关联的订单id
                    orderItem.setOrderId(orderId);
                    //商品累计总金额
                    money += orderItem.getTotalFee().doubleValue();

                    //保存订单商品明细
                    orderItemMapper.insertSelective(orderItem);
                }

                //实付金额
                shopOrder.setPayment(new BigDecimal(money));
                //保存订单
                orderMapper.insertSelective(shopOrder);


                //将订单id存储到订单号集合
                orderList.add(String.valueOf(orderId));
                //记录总金额
                totalMoney += money;
            }

            //判断是微信支付还是货到付款
            if ("1".equals(order.getPaymentType())) {

                //创建日志对象
                PayLog payLog = new PayLog();
                /** 生成订单交易号 */
                String outTradeNo = String.valueOf(idWorker.nextId());
                //封装支付订单号
                payLog.setOutTradeNo(outTradeNo);
                //封装创建订单号时间
                payLog.setCreateTime(new Date());
                //封装支付总金额
                payLog.setTotalFee((long)(totalMoney * 100));
                //封装用户名
                payLog.setUserId(order.getUserId());
                //交易状态
                payLog.setTradeState("0");

                //订单号集合进行格式化，变成val1,val2,val3
                String orderIds = orderList.toString().replace("[", "")
                        .replace("]", "").replace(" ", "");
                //封装订单号
                payLog.setOrderList(orderIds);
                //封装支付类型
                payLog.setPayType("1");

                //添加日志信息到数据库
                payLogMapper.insertSelective(payLog);

                //把日志信息暂时放入redis缓存
                redisTemplate.boundValueOps("payLog_" + order.getUserId()).set(payLog);
            }

            //删除用户已下订单的购物车商品
            redisTemplate.delete("cart_" + order.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改方法
     *
     * @param order
     */
    @Override
    public void update(Order order) {

    }

    /**
     * 根据主键id删除
     *
     * @param id
     */
    @Override
    public void delete(Serializable id) {

    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Order findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Order> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param order
     * @param page
     * @param rows
     */
    @Override
    public List<Order> findByPage(Order order, int page, int rows) {
        return null;
    }

    /**
     * 根据用户名获取reids中的日志对象
     *
     * @param loginName
     * @return PayLog
     */
    @Override
    public PayLog findPayLogFormRedis(String loginName) {

        try {
            return (PayLog) redisTemplate.boundValueOps("payLog_" + loginName).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改订单状态
     *
     * @param outTradeNo     支付订单号
     * @param transaction_id 微信交易流水号
     */
    @Override
    public void updateOrderStatus(String outTradeNo, String transaction_id) {

        try {
            /*修改支付日志*/
            //根据支付订单号获取日志
            PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
            //修改支付时间
            payLog.setPayTime(new Date());
            //修改支付状态
            payLog.setTradeState("1");
            //修改交易号
            payLog.setTransactionId(transaction_id);
            //根据日志id修改日志
            payLogMapper.updateByPrimaryKeySelective(payLog);


            /*修改订单状态*/
            //获取该日志的所有订单
            String[] orderArr = payLog.getOrderList().split(",");

            //遍历订单数组
            for (String orderId : orderArr) {
                Order order = new Order();
                //设置订单id
                order.setOrderId(Long.valueOf(orderId));
                //修改支付时间
                order.setPaymentTime(new Date());
                //修改订单状态
                order.setStatus("2");
                //根据订单id修改订单
                orderMapper.updateByPrimaryKeySelective(order);
            }

            //清除缓存redis中的支付日志
            redisTemplate.delete("payLog_" + payLog.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
