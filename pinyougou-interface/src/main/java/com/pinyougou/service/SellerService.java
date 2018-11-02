package com.pinyougou.service;

import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SellerService类
 * @Date: 2018/9/29 17:31
 * @Description:商家服务接口
 * @Version：1.0
 */

public interface SellerService {

	/** 添加方法 */
	void save(Seller seller);

	/** 修改方法 */
	void update(Seller seller);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Seller findOne(Serializable id);

	/** 查询全部 */
	List<Seller> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Seller seller, int page, int rows);

	/**
	 * 修改商家的状态（审核商家）
	 * @param sellerId
	 * @param status
	 */
    void updateStatus(String sellerId, String status);
}