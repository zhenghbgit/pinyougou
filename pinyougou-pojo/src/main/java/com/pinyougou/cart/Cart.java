package com.pinyougou.cart;

import com.pinyougou.pojo.OrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：Cart类
 * @Date: 2018/10/28 16:11
 * @Description:商家购物车实体类,对每个商家的购物车进行的封装
 * @Version：1.0
 */

public class Cart implements Serializable {

    // 商家ID
    private String sellerId;
    // 商家名称
    private String sellerName;
    // 购物车明细集合
    private List<OrderItem> orderItems;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
