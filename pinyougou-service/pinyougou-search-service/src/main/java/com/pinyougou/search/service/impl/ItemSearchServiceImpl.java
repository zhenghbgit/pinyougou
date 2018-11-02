package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：searchServiceImpl类
 * @Date: 2018/10/18 9:17
 * @Description:搜索服务
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 根据条件搜索商品
     *
     * @param itemMap
     * @return Map<String,Object>
     */
    @Override
    public Map<String, Object> searchItem(Map<String, Object> itemMap) {

        //创建map集合，用于封装检索到的数据
        Map<String, Object> dataMap = new HashMap<>();

        //获取查询关键字
        String keywords = (String) itemMap.get("keywords");

        //获取当前页
        Integer page = (Integer) itemMap.get("page");
        //判断传进来的当前页是否为空或者page小于1
        if (page == null || page < 1) {
            //true , 则默认给它默认值(第一页)
            page = 1;
        }
        //获取页面大小
        Integer row = (Integer) itemMap.get("row");
        //判断当前页面大小是否为空
        if (row == null) {
            //true,则默认给它默认值（10条）
            row = 10;
        }

        //判断是否有条件
        if (StringUtils.isNoneEmpty(keywords)) {
            //当有条件的时候，根据条件高亮查询

            //创建高亮查询对象
            HighlightQuery highlightQuery = new SimpleHighlightQuery();
            //创建高亮选项对象
            HighlightOptions highlightOptions = new HighlightOptions();
            //添加需要高亮的字段
            highlightOptions.addField("title");
            //添加前缀
            highlightOptions.setSimplePrefix("<font style='font-weight: bold' color='red' >");
            //添加后缀
            highlightOptions.setSimplePostfix("</font>");
            //添加高亮选项
            highlightQuery.setHighlightOptions(highlightOptions);

            //创建查询条件对象
            Criteria criteria = new Criteria("keywords").is(keywords);
            //添加查询条件
            highlightQuery.addCriteria(criteria);

            //分类、规格选项
            //获取分类
            String category = (String) itemMap.get("category");
            //判断分类字段是否为空
            if (!"".equals(category)) {
                //创建条件查询对象
                Criteria criteria1 = new Criteria("category").is(category);
                //创建过滤查询对象
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                //添加过滤查询
                highlightQuery.addFilterQuery(filterQuery);

            }

            //获取品牌
            String brand = (String) itemMap.get("brand");
            if (!"".equals(brand)) {
                //创建条件对象
                Criteria criteria1 = new Criteria("brand").is(brand);
                //创建过滤查询对象
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                //添加过滤查询
                highlightQuery.addFilterQuery(filterQuery);
            }

            //动态域
            //判断规格是否为空
            Object spec = itemMap.get("spec");

            if (itemMap.get("spec") != null) {
                //如果不为空，获取spec,{"网络" ："移动4G" , "机身内存" : "16G"}转化为map集合
                Map<String, String> map = (Map) itemMap.get("spec");
                //迭代map的键
                for (String key : map.keySet()) {
                    //创建条件对象
                    Criteria criteria1 = new Criteria("spec_" + key).is(map.get(key));
                    //创建过滤查询对象
                    SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                    //添加过滤查询
                    highlightQuery.addFilterQuery(filterQuery);
                }
            }

            //获取价格
            String price = (String) itemMap.get("price");
            //判断价格是否为空
            if (!"".equals(price)) {

                //分割价格字符串
                String[] splitPrice = price.split("-");

                //判断第一个价格是否为0
                if (!"0".equals(splitPrice[0])) {
                    //创建条件对象
                    Criteria criteria1 = new Criteria("price").greaterThanEqual(splitPrice[0]);
                    //创建过滤查询对象
                    SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                    //添加过滤查询
                    highlightQuery.addFilterQuery(filterQuery);
                }

                //判断第二个价格是否为*
                if (!"*".equals(splitPrice[1])) {
                    //创建条件对象
                    Criteria criteria1 = new Criteria("price").lessThanEqual(splitPrice[1]);
                    //创建过滤查询对象
                    SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                    //添加过滤查询
                    highlightQuery.addFilterQuery(filterQuery);
                }

            }

            //排序
            //获取排序的域与获取排序的方式
            String sortField = (String) itemMap.get("sortField");
            String sortValue = (String) itemMap.get("sort");

            //如果排序域和排序方式不为空，则按域与排序方式进行排序
            if (StringUtils.isNoneEmpty(sortField) && StringUtils.isNoneEmpty(sortValue)) {

                //创建排序对象
                Sort sort = new Sort("ASC".equalsIgnoreCase(sortValue) ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);

                //添加排序
                highlightQuery.addSort(sort);
            }

            //设置起始记录查询数
            highlightQuery.setOffset((page - 1) * row);
            //设置页面显示记录数
            highlightQuery.setRows(row);

            //根据条件分页高亮检索数据,得到高亮分页查询对象
            HighlightPage<SolrItem> itemHighlightPage = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);

            //获取高亮项集合
            List<HighlightEntry<SolrItem>> highlighted = itemHighlightPage.getHighlighted();

            //遍历高亮项集合
            for (HighlightEntry<SolrItem> highlightEntry : highlighted) {

                //获取检索到的原实体对象
                SolrItem solrItem = highlightEntry.getEntity();
                //判断高亮集合是否有值且高亮第一个字段的高亮内容是否有值
                if (highlightEntry.getHighlights().size() > 0
                        && highlightEntry.getHighlights().get(0).getSnipplets().size() > 0) {
                    //把高亮的结果封装回solrItem
                    solrItem.setTitle(highlightEntry.getHighlights().get(0).getSnipplets().get(0));

                }
            }

            //总页数
            dataMap.put("totalPage", itemHighlightPage.getTotalPages());
            //总记录数
            dataMap.put("total", itemHighlightPage.getTotalElements());
            dataMap.put("row", itemHighlightPage.getContent());
        }else {
            //如果没有查询条件，就执行简单查询
            Query query = new SimpleQuery("*:*");

            //设置起始记录查询数
            query.setOffset((page - 1) * row);
            //设置页面显示记录数
            query.setRows(row);

            //分页查询
            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);

            //总页数
            dataMap.put("totalPage", solrItems.getTotalPages());
            //总记录数
            dataMap.put("total", solrItems.getTotalElements());
            dataMap.put("row", solrItems.getContent());
        }


        return dataMap;
    }

    /**
     * 添加商品数据到索引库
     *
     * @param solrItemList
     */
    @Override
    public void saveOrUpdate(List<SolrItem> solrItemList) {

        //添加数据到索引库，返回响应对象
        UpdateResponse response = solrTemplate.saveBeans(solrItemList);
        //从响应对象中获取状态,判断是否添加成功
        if (response.getStatus() == 0) {
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }

    }

    /**
     * 根据商品id删除索引库中的该商品索引
     *
     * @param goodsIds
     */
    @Override
    public void deleteItem(Long[] goodsIds) {

        //创建查询对象
        Query query = new SimpleQuery();
        //创建条件
        Criteria criteria = new Criteria("goodsId").in(goodsIds);
        //添加条件到查询对象
        query.addCriteria(criteria);
        //查询,返回响应对象
        UpdateResponse response = solrTemplate.delete(query);
        //判断是否上传成功
        if (response.getStatus() == 0) {
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }

    }

    /**
     * 根据条件搜索商品(不完整，没有高亮)
     *
     * @param itemMap
     * @return Map<String , Object>
     */
    /*@Override
    public Map<String, Object> searchItem(Map<String, Object> itemMap) {

        //创建map集合，用于封装检索到的数据
        Map<String, Object> map = new HashMap<>();

        //创建查询对象
        Query query = new SimpleQuery("*:*");

        //获取查询关键字
        String keywords = (String) itemMap.get("keywords");

        //判断检索关键字是否为空
        if (StringUtils.isNoneEmpty(keywords)) {
            //创建条件对象
            Criteria criteria = new Criteria("keywords").is(keywords);
            //添加查询条件
            query.addCriteria(criteria);
        }

        //分页检索数据
        ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);

        //封装数据
        map.put("row", solrItems.getContent());

        return map;
    }*/


}
