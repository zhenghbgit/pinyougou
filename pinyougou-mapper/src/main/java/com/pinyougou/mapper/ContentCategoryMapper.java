package com.pinyougou.mapper;

import com.pinyougou.pojo.ContentCategory;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentCategoryMapper接口
 * @Date: 2018/10/14 10:48
 * @Description:广告类型管理持久层接口
 * @Version：1.0
 */

public interface ContentCategoryMapper extends Mapper<ContentCategory> {

    void deleteContentCategoryById(Serializable[] ids);
}
