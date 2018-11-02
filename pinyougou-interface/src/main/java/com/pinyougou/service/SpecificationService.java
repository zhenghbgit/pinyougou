package com.pinyougou.service;

import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SpecificationService类
 * @Date: 2018/9/29 17:32
 * @Description:规格服务接口
 * @Version：1.0
 */

public interface SpecificationService {

	/** 添加方法 */
	void save(Specification specification);

	/** 修改方法 */
	void update(Specification specification);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Specification findOne(Serializable id);

	/** 查询全部 */
	List<Specification> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Specification specification, int page, int rows);

	/**
	 * 根据规格id查询规格选项
	 * @param id
	 * @return List<SpecificationOption>
	 */
    List<SpecificationOption> findSpecificationOption(Long id);

	/**
	 * 查询所有规格信息
	 * @return List<Map<String, Object>>
	 */
	List<Map<String,Object>> findSpecificationList();
}