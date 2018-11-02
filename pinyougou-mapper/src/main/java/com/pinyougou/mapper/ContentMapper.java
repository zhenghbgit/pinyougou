package com.pinyougou.mapper;

import com.pinyougou.pojo.Content;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentMapper接口
 * @Date: 2018/10/14 14:48
 * @Description:广告管理持久层接口
 * @Version：1.0
 */

public interface ContentMapper extends Mapper<Content> {

    /**
     * 根据广告分类查询广告
     * @param categoryId
     * @return List<Content>
     */
    @Select("select * from tb_content where category_id = #{categoryId} and status = 1 ORDER BY sort_order ASC")
    List<Content> findContentByCategoryId(Long categoryId);
}
