package com.pinyougou.service;

import com.pinyougou.pojo.FreightTemplate;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：FreightTemplateService类
 * @Date: 2018/9/29 17:28
 * @Description:运费模板服务接口
 * @Version：1.0
 */

public interface FreightTemplateService {

	/** 添加方法 */
	void save(FreightTemplate freightTemplate);

	/** 修改方法 */
	void update(FreightTemplate freightTemplate);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	FreightTemplate findOne(Serializable id);

	/** 查询全部 */
	List<FreightTemplate> findAll();

	/** 多条件分页查询 */
	List<FreightTemplate> findByPage(FreightTemplate freightTemplate, int page, int rows);

}