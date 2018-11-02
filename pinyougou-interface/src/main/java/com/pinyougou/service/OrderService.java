package com.pinyougou.service;

import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;

import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：OrderService类
 * @Date: 2018/9/29 17:30
 * @Description:订单服务接口
 * @Version：1.0
 */

public interface OrderService {

	/** 添加方法 */
	void save(Order order);

	/** 修改方法 */
	void update(Order order);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Order findOne(Serializable id);

	/** 查询全部 */
	List<Order> findAll();

	/** 多条件分页查询 */
	List<Order> findByPage(Order order, int page, int rows);

	/**
	 * 根据用户名获取reids中的日志对象
	 * @param loginName
	 * @return PayLog
	 */
    PayLog findPayLogFormRedis(String loginName);

	/**
	 * 修改订单状态
	 * @param outTradeNo 订单号
	 * @param transaction_id 微信交易流水号
	 */
	void updateOrderStatus(String outTradeNo, String transaction_id);
}