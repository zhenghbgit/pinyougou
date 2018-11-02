package com.pinyougou.solr.util;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SolrUtils类
 * @Date: 2018/10/17 22:28
 * @Description:查询SKU商品数据，批量导入到索引库类
 * @Version：1.0
 */

@Component
public class SolrUtils {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品数据到索引库
     */
    public void imporItemNotDatabase() {

        //实例化Item对象
        Item item = new Item();
        //查询正常商品
        item.setStatus("1");

        //从数据库中查询正常的商品数据
        List<Item> itemList = itemMapper.select(item);

        //创建索引库实体类集合
        List<SolrItem> solrItemList = new ArrayList<>();

        //将数据库中获取的item转化为索引库中的item,并储存到solrItemList集合
        for (Item item1 : itemList) {

            SolrItem solrItem = new SolrItem();
            solrItem.setId(item1.getId());
            solrItem.setTitle(item1.getTitle());
            solrItem.setPrice(item1.getPrice());
            solrItem.setImage(item1.getImage());
            solrItem.setGoodsId(item1.getGoodsId());
            solrItem.setCategory(item1.getCategory());
            solrItem.setBrand(item1.getBrand());
            solrItem.setSeller(item1.getSeller());
            solrItem.setUpdateTime(item1.getUpdateTime());

            //将spec字段的json字符串格式转化为map对象
            Map map = JSON.parseObject(item1.getSpec(), Map.class);
            //设置动态域
            solrItem.setSpec_map(map);

            solrItemList.add(solrItem);

        }


        //保存到索引库,返回响应对象
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItemList);

        //判断响应状态
        if (updateResponse.getStatus() == 0) {
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }

    }

    //删除全部文档
    public void delete() {
        Query query = new SimpleQuery("*:*");
        UpdateResponse updateResponse = solrTemplate.delete(query);
        if (updateResponse.getStatus() == 0) {
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
    }

    public static void main(String[] args) {

        //加载spring配置文件
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        //从容器中获取
        SolrUtils solrUtils = applicationContext.getBean(SolrUtils.class);
        //执行方法
        solrUtils.imporItemNotDatabase();

    }


}
