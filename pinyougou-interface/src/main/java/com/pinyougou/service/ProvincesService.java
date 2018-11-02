package com.pinyougou.service;

import com.pinyougou.pojo.Provinces;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ProvincesService类
 * @Date: 2018/9/29 17:30
 * @Description:省份服务接口
 * @Version：1.0
 */

public interface ProvincesService {

	/** 添加方法 */
	void save(Provinces provinces);

	/** 修改方法 */
	void update(Provinces provinces);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Provinces findOne(Serializable id);

	/** 查询全部 */
	List<Provinces> findAll();

	/** 多条件分页查询 */
	List<Provinces> findByPage(Provinces provinces, int page, int rows);

}