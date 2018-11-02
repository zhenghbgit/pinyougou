package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemMessageListener类
 * @Date: 2018/10/24 21:52
 * @Description:商品消息监听者
 * @Version：1.0
 */

public class ItemMessageListener implements SessionAwareMessageListener<ObjectMessage> {

    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {

        //获取消息内容
        Long[] goodsIds = (Long[]) objectMessage.getObject();

        //根据商品id搜索商品SKU
        List<Item> itemList = goodsService.findItemByGoodIds(goodsIds);
        //判断是否搜索到有值
        if (itemList.size() > 0) {
            //创建商品的索引实体类对象
            List<SolrItem> solrItemList = new ArrayList<>();

            //遍历商品SKU集合，把数据转化成solrItem
            for (Item item : itemList) {
                SolrItem solrItem = new SolrItem();
                solrItem.setId(item.getId());
                solrItem.setTitle(item.getTitle());
                solrItem.setPrice(item.getPrice());
                solrItem.setImage(item.getImage());
                solrItem.setGoodsId(item.getGoodsId());
                solrItem.setCategory(item.getCategory());
                solrItem.setBrand(item.getBrand());
                solrItem.setSeller(item.getSeller());
                solrItem.setUpdateTime(item.getUpdateTime());

                //把规格数据的JSON字符串格式转化成Map
                Map map = JSON.parseObject(item.getSpec(), Map.class);
                solrItem.setSpec_map(map);

                //添加数据
                solrItemList.add(solrItem);
            }

            //修改索引库（添加/修改商品索引库）
            itemSearchService.saveOrUpdate(solrItemList);
        }


    }
}
