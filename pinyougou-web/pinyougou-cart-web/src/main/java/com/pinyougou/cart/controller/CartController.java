package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.Cart;
import com.pinyougou.service.CartService;
import com.pinyougou.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：CartController类
 * @Date: 2018/10/28 19:39
 * @Description:购物车控制器
 * @Version：1.0
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    //引入购物车服务
    @Reference(timeout = 10000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 添加SKU商品到购物车,存放到Cookie
     * @param itemId 商品ID
     * @param num  数量
     * @return boolean
     */
    @GetMapping("addCart")
    //origins 允许访问的域名(跨域发请求的域名可以访问该域名)
    //allowCredentials 允许操作该域名下的Cookie
    //该Spring注解代替了下面的跨域请求配置
    @CrossOrigin(origins = "http://item.pinyougou.com",allowCredentials = "true")
    public boolean addCart(Long itemId,Integer num) {

        try {
            //判断用户是否登陆
            //获取登陆用户名
            String loginUserName = request.getRemoteUser();

            //获取购物车
            List<Cart> carts = findCarts();

            //把商品添加到购物车
            carts = cartService.addCart(carts, itemId, num);

            //判断是否登陆用户
            if (StringUtils.isNoneBlank(loginUserName)) {
                //把购物车数据存到Redis中
                cartService.saveCartsToRedis(carts,loginUserName);
            } else {
                //说明：Cookie的value存储List<Cart>的json格式字符串数据
                // (中文需要unicode编码，因为Cookie的值不能存在中文字符)
                //将购物车转化为JSON格式字符串
                String cartsJson = JSON.toJSONString(carts);

                /*//跨域请求配置
                //设置允许访问的域名
                response.setHeader("Access-Control-Allow-Origin", "http://item.pinyougou.com");
                //设置允许操作该域名下的Cookie
                response.setHeader("Access-Control-Allow-Credentials", "true");*/

                //把购物车存放到Cookie
                CookieUtils.setCookie(request, response, CookieUtils.CookieName.PINYOUGOU_CART,
                        cartsJson, 60*60*24, true);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取购物车
     * @return
     */
    @GetMapping("findCarts")
    public List<Cart> findCarts() {

        //创建购物车集合
        List<Cart> carts = null;

        //判断是否已经登陆,根据用户名是否为空来判断
        //获取用户名
        String loginUserName = request.getRemoteUser();
        //判断是否为空
        if (StringUtils.isNoneBlank(loginUserName)) {
            //从Redis中获取购物车数据
            carts = cartService.findCartsFromRedis(loginUserName);

            //从Cookie中获取购物车数据
            String cookieValue = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART, true);

            //判断从Cookie中获取到用户购物车
            if (StringUtils.isNoneBlank(cookieValue)) {
                //将用户购物车数据转化为List集合
                List<Cart> cookieCarts = JSON.parseArray(cookieValue, Cart.class);

                //判断转化后用户购物车集合是否为空
                if (cookieCarts != null && cookieCarts.size() > 0) {
                    //合并购物车
                    carts = cartService.mergeCarts(carts, cookieCarts);
                    //合并后购物车数据保存到redis,并删除Cookie购物车
                    cartService.saveCartsToRedis(carts, loginUserName);
                    CookieUtils.deleteCookie(request, response, CookieUtils.CookieName.PINYOUGOU_CART);
                }

            }
        }else {
            //从Cookie中获取购物车数据
            String cookieValue = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART, true);

            //判断是否获取到为空
            if (StringUtils.isBlank(cookieValue)) {
                cookieValue = "[]";
            }

            //将字符串转化为JSON对象
            carts = JSON.parseArray(cookieValue, Cart.class);
        }

        return carts;
    }

}
