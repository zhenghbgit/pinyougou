package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：GoodsMapper接口
 * @Date: 2018/10/10 16:20
 * @Description:商品持久层接口
 * @Version：1.0
 */

public interface GoodsMapper extends Mapper<Goods> {

    /**
     * 根据条件查询所有的商品
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> findAllGoods(Goods goods);

    /**
     * 修改商品状态
     * @param status
     */
    void updataStatus(@Param("ids") Long[] ids, @Param("status") String status,@Param("typeName") String typeName);

    /**
     * 根据商品ids查询审批通过的商品
     * @param ids
     * @return List<Goods>
     */
    List<Goods> findGoodsByIds(Long[] ids);
}
