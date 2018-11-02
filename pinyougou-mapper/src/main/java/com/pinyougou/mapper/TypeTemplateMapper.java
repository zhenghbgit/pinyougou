package com.pinyougou.mapper;

import com.pinyougou.pojo.TypeTemplate;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：TypeTemplateMapper接口
 * @Date: 2018/10/5 11:45
 * @Description:模板持久层接口
 * @Version：1.0
 */

public interface TypeTemplateMapper extends Mapper<TypeTemplate> {

    /**
     * 根据模板名称查询模板
     * @param typeTemplate
     * @return List<TypeTemplate>
     */
    List<TypeTemplate> findTypeTemplateByTypeTemplateName(TypeTemplate typeTemplate);

    /**
     * 查询所有模板列表
     * @return List<Map<String,Object>>
     */
    @Select("select id,name as text from tb_type_template")
    List<Map<String,Object>> findTypeTemplateList();
}
