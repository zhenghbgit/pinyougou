package com.pinyougou.service;

import com.pinyougou.pojo.Areas;

import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：AreasService类
 * @Date: 2018/9/29 17:25
 * @Description:区域服务接口
 * @Version：1.0
 */

public interface AreasService {

    /**
     * 添加方法
     */
    void save(Areas areas);

    /**
     * 修改方法
     */
    void update(Areas areas);

    /**
     * 根据主键id删除
     */
    void delete(Serializable id);

    /**
     * 批量删除
     */
    void deleteAll(Serializable[] ids);

    /**
     * 根据主键id查询
     */
    Areas findOne(Serializable id);

    /**
     * 查询全部
     */
    List<Areas> findAll();

    /**
     * 多条件分页查询
     */
    List<Areas> findByPage(Areas areas, int page, int rows);

}