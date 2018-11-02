package com.pinyougou.service;

import com.pinyougou.cart.Cart;

import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：CartService接口
 * @Date: 2018/10/28 19:44
 * @Description:购物车服务接口
 * @Version：1.0
 */

public interface CartService {

    /**
     * 将商品添加到购物车
     * @param itemId SKU商品id
     * @param num 数量
     * @param carts 购物车数据
     * @return List<Cart>
     */
    List<Cart> addCart(List<Cart> carts, Long itemId, Integer num);

    /**
     * 根据用户在Redis中查找该用户的购物车
     * @param loginUserName 用户名
     * @return List<Cart>
     */
    List<Cart> findCartsFromRedis(String loginUserName);

    /**
     * 根据用户保存用户购物车到Redis
     * @param carts
     * @param loginUserName
     */
    void saveCartsToRedis(List<Cart> carts,String loginUserName);

    /**
     * 合并购物车
     * @param carts
     * @param cookieCarts
     */
    List<Cart> mergeCarts(List<Cart> carts, List<Cart> cookieCarts);
}
