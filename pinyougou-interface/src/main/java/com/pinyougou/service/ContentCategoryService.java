package com.pinyougou.service;

import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.pojo.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentCategoryService类
 * @Date: 2018/9/29 17:27
 * @Description:广告分类服务接口
 * @Version：1.0
 */

public interface ContentCategoryService {

	/** 添加方法 */
	void save(ContentCategory contentCategory);

	/** 修改方法 */
	void update(ContentCategory contentCategory);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	ContentCategory findOne(Serializable id);

	/** 查询全部 */
	List<ContentCategory> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(ContentCategory contentCategory, int page, int rows);

}