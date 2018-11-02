package com.pinyougou.service;

import com.pinyougou.pojo.Content;
import com.pinyougou.pojo.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentService类
 * @Date: 2018/9/29 17:27
 * @Description:广告服务接口
 * @Version：1.0
 */

public interface ContentService {

	/** 添加方法 */
	void save(Content content);

	/** 修改方法 */
	void update(Content content);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Content findOne(Serializable id);

	/** 查询全部 */
	List<Content> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Content content, int page, int rows);

	/**
	 * 根据广告分类查询广告
	 * @param categoryId
	 * @return List<Content>
	 */
    List<Content> findContentByCategoryId(Long categoryId);
}