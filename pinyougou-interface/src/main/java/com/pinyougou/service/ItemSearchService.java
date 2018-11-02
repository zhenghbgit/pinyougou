package com.pinyougou.service;

import com.pinyougou.solr.SolrItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemSearchService接口
 * @Date: 2018/10/18 9:18
 * @Description:商品搜索服务接口
 * @Version：1.0
 */

public interface ItemSearchService {

    /**
     * 根据条件搜索商品
     * @param itemMap
     * @return Map<String, Object>
     */
    Map<String,Object> searchItem(Map<String,Object> itemMap);

    /**
     * 添加商品数据到索引库
     * @param solrItemList
     */
    void saveOrUpdate(List<SolrItem> solrItemList);

    /**
     * 根据商品id删除索引库中的该商品索引
     * @param goodsIds
     */
    void deleteItem(Long[] goodsIds);
}
