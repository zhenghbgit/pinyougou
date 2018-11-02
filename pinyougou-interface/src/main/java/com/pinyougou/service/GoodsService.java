package com.pinyougou.service;

import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.PageResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：GoodsService类
 * @Date: 2018/9/29 17:28
 * @Description:商品服务接口
 * @Version：1.0
 */

public interface GoodsService {

	/** 添加方法 */
	void save(Goods goods);

	/** 修改方法 */
	void update(Goods goods);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Goods findOne(Serializable id);

	/** 查询全部 */
	List<Goods> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Goods goods, int page, int rows);

	/**
	 * 修改商品状态
	 * @param status	修改的状态
	 * @param ids		要修改的id
	 * @param typeName 方法名
	 */
    void updataStatus(Long[] ids,String status,String typeName);

	/**
	 * 根据商品id查询商品详情信息
	 * @param goodsId
	 * @return Map<String,Object>
	 */
	Map<String,Object> findGoodsByGoodsId(Long goodsId);

	/**
	 * 根据商品id查询商品SKU
	 * @param goodsIds
	 * @return
	 */
	List<Item> findItemByGoodIds(Long[] goodsIds);
}