package com.pinyougou.service;

import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.PageResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：BrandService接口
 * @Date: 2018/9/28 15:56
 * @Description:品牌服务接口
 * @Version：1.0
 */

public interface BrandService {

    /** 添加方法 */
    void save(Brand brand);

    /** 修改方法 */
    void update(Brand brand);

    /** 根据主键id删除 */
    void delete(Serializable id);

    /** 批量删除 */
    void deleteAll(Serializable[] ids);

    /** 根据主键id查询 */
    Brand findOne(Serializable id);

    /** 查询全部 */
    List<Brand> findAll();

    /** 多条件分页查询 */
    PageResult findByPage(Brand brand, int page, int rows);

    /**
     * 查询所有品牌信息(Id和Name)
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> findAllIdAndName();

}
