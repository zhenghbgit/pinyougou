package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SpecificationOptionMapper接口
 * @Date: 2018/10/4 12:47
 * @Description:规格选线持久层接口
 * @Version：1.0
 */

public interface SpecificationOptionMapper extends Mapper<SpecificationOption> {

    /**
     * 保存规格选项信息
     * @param specification
     */
    void save(Specification specification);


}
