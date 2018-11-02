package com.pinyougou.service;

import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import java.util.List;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：TypeTemplateService类
 * @Date: 2018/9/29 17:33
 * @Description:类型模板服务接口
 * @Version：1.0
 */

public interface TypeTemplateService {

	/** 添加方法 */
	void save(TypeTemplate typeTemplate);

	/** 修改方法 */
	void update(TypeTemplate typeTemplate);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	TypeTemplate findOne(Serializable id);

	/** 查询全部 */
	List<TypeTemplate> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(TypeTemplate typeTemplate, int page, int rows);

	/**
	 * 查询所有模板列表
	 * @return List<Map<String,Object>>
	 */
    List<Map<String,Object>> findTypeTemplateList();

	/**
	 * 根据模板id查找模板（获取规格与规格选项）
	 * @param id
	 * @return List<Map>
	 */
	List<Map> typeTemplateService(Long id);
}