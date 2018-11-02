package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SpecificationMapper接口
 * @Date: 2018/10/3 15:11
 * @Description:规格持久层接口
 * @Version：1.0
 */

public interface SpecificationMapper extends Mapper<Specification>{

    /**
     * 根据条件查询规格
     * @param specification
     * @return List<Specification>
     */
    List<Specification> findSpecificationBySpecificationName(Specification specification);

    /**
     * 查询所有规格信息(id和Name)
     * @return List<Map<String,Object>>
     */
    @Select("select id,spec_name as text from tb_specification order by id asc")
    List<Map<String,Object>> findSpecificationList();
}
