package com.pinyougou.service;

import com.pinyougou.pojo.ItemCat;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemCatService类
 * @Date: 2018/9/29 17:29
 * @Description:商品类型(类目)服务接口
 * @Version：1.0
 */

public interface ItemCatService {

	/** 添加方法 */
	void save(ItemCat itemCat);

	/** 修改方法 */
	void update(ItemCat itemCat);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	ItemCat findOne(Serializable id);

	/** 查询全部 */
	List<ItemCat> findAll();

	/** 多条件分页查询 */
	List<ItemCat> findByPage(ItemCat itemCat, int page, int rows);

	/**
	 * 查询分类列表（最上级）
	 * @param parentId
	 * @return
	 */
    List<ItemCat> findItemCatByParentId(Long parentId);

	/**
	 * 根据数组parentId查询分类列表
	 * @param parentId
	 * @return List<ItemCat>
	 */
	List<ItemCat> findItemCatByParentId(Serializable[] parentId);

}