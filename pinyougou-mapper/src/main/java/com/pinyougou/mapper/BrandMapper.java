package com.pinyougou.mapper;

import com.pinyougou.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：BrandMapper接口
 * @Date: 2018/9/28 10:15
 * @Description:品牌持久层接口
 * @Version：1.0
 */

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据条件查询品牌信息
     * @param brand
     * @return List<Brand>
     */
    List<Brand> findBrandByTerm(Brand brand);

    /**
     * 查询所有品牌信息（Id和Name）
     * @return List<Map<String, Object>>
     */
    @Select("select id, name as text from tb_brand order by id asc")
    List<Map<String, Object>> findAllIdAndName();
}
