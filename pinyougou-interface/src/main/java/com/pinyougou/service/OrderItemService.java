package com.pinyougou.service;

import com.pinyougou.pojo.OrderItem;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：OrderItemService类
 * @Date: 2018/9/29 17:30
 * @Description:订单类型服务接口
 * @Version：1.0
 */

public interface OrderItemService {

	/** 添加方法 */
	void save(OrderItem orderItem);

	/** 修改方法 */
	void update(OrderItem orderItem);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	OrderItem findOne(Serializable id);

	/** 查询全部 */
	List<OrderItem> findAll();

	/** 多条件分页查询 */
	List<OrderItem> findByPage(OrderItem orderItem, int page, int rows);

}