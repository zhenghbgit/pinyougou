package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.Cart;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：CartServiceImpl类
 * @Date: 2018/10/28 19:45
 * @Description:购物车服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.CartService")
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 将商品添加到购物车
     *
     * @param itemId
     * @param num
     * @param carts
     * @return List<Cart>
     */
    @Override
    public List<Cart> addCart(List<Cart> carts,Long itemId, Integer num) {

        try {
            //根据SKU商品ID查询商品
            Item item = itemMapper.selectByPrimaryKey(itemId);
            //获取商家ID
            String sellerId = item.getSellerId();

            //根据用户购物车和商家ID，搜索用户购物车是否存在同一家商家的购物车
            Cart cart = searchCart(carts,sellerId);

            //判断是否存在该商品的商家购物车
            if (cart == null) {
                //没有该商品的商家购物车,创建该商家购物车
                cart = new Cart();

                cart.setSellerId(sellerId);
                cart.setSellerName(item.getSeller());

                //创建商品订单
                OrderItem orderItem = createOrderItem(item,num);
                List<OrderItem> orderItemList = new ArrayList<>();
                orderItemList.add(orderItem);

                cart.setOrderItems(orderItemList);

                //用户购物车添加商家购物车
                carts.add(cart);
            }else {
                //存在该商品的商家购物车

                //搜索该商家购物车，是否存在该商品
                OrderItem orderItem = searchItemForCart(cart.getOrderItems(), itemId);
                if (orderItem == null) {
                    //该商家购物车不存在该商品,直接创建商品订单
                    orderItem = createOrderItem(item, num);
                    //把商品订单添加到商家购物车
                    cart.getOrderItems().add(orderItem);
                }else {
                    //该商家购物车存在该商品，修改该商品的数量和总金额
                    //修改商品数量
                    orderItem.setNum(orderItem.getNum() + num);
                    //修改总金额
                    orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));


                    //判断该商品数量
                    if (orderItem.getNum() <= 0) {
                        //如果该商品小于1，移除商品
                        cart.getOrderItems().remove(orderItem);
                    }

                    //判断该商家的购物车里的商品数量
                    if (cart.getOrderItems().size() <= 0) {
                        carts.remove(cart);
                    }

                }
            }

            return carts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 搜索商家购物车是否存在该商品
     * @param orderItems
     * @param itemId
     * @return OrderItem
     */
    private OrderItem searchItemForCart(List<OrderItem> orderItems, Long itemId) {

        //遍历商家购物车
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getItemId().equals(itemId)) {
                //存在该商品，直接返回
                return orderItem;
            }
        }

        //不存在改商品
        return null;
    }

    /**
     * 根据用户购物车和加入的商品的商家id搜索是否存在该商家购物车
     * @param carts
     * @param sellerId
     * @return
     */
    private Cart searchCart(List<Cart> carts, String sellerId) {

        //遍历用户购物车
        for (Cart cart : carts) {
            //从商家购物车获取商家id,判断是否存在
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }

        //没有该商品的商家购物车
        return null;
    }

    /**
     * 创建商品详细订单
     * @param item
     * @param num
     * @return OrderItem
     */
    private OrderItem createOrderItem(Item item, Integer num) {

        //创建商品详情订单
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        //小计
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));

        return orderItem;
    }






    /**
     * 根据用户在Redis中查找该用户的购物车
     *
     * @param loginUserName 用户名
     * @return List<Cart>
     */
    @Override
    public List<Cart> findCartsFromRedis(String loginUserName) {

        System.out.println("获取Redis中的购物车：" + loginUserName);
        List<Cart> carts = (List<Cart>) redisTemplate.boundValueOps("cart_" + loginUserName).get();

        //判断是否从Redis中获取到数据
        if (carts == null) {
            carts = new ArrayList<>();
        }

        return carts;
    }

    /**
     * 保存用户购物车到Redis
     *
     * @param carts
     * @param loginUserName
     */
    @Override
    public void saveCartsToRedis(List<Cart> carts,String loginUserName) {
        redisTemplate.boundValueOps("cart_" + loginUserName).set(carts);
    }

    /**
     * 合并购物车
     *
     * @param carts
     * @param cookieCarts
     * @return List<Cart>
     */
    @Override
    public List<Cart> mergeCarts(List<Cart> carts, List<Cart> cookieCarts) {


        //遍历Cookie中的购物车
        for (Cart cookieCart : cookieCarts) {

            //遍历购物车中的商品
            for (OrderItem orderItem : cookieCart.getOrderItems()) {
                //把商品添加到redis购物车
                carts = addCart(carts, orderItem.getItemId(), orderItem.getNum());
            }

        }

        return carts;
    }

}
