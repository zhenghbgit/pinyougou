package com.pinyougou.solr;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SolrItem类
 * @Date: 2018/10/17 22:46
 * @Description:Item索引库实体类
 * @Version：1.0
 */

public class SolrItem implements Serializable {

    //属性是用@Field
    //如果属性与配置文件定义的域名称不一致，则要在Field中指定名称
    @Field
    private Long id;
    @Field("title")
    private String title;
    @Field("price")
    private double price;
    @Field("image")
    private String image;
    @Field("goodsId")
    private Long goodsId;
    @Field("category")
    private String category;
    @Field("brand")
    private String brand;
    @Field("seller")
    private String seller;
    @Field("updateTime")
    private Date updateTime;

    @Dynamic
    @Field("spec_*")
    private Map<String, String> spec_map;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.doubleValue();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, String> getSpec_map() {
        return spec_map;
    }

    public void setSpec_map(Map<String, String> spec_map) {
        this.spec_map = spec_map;
    }
}
