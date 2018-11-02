package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SellerController类
 * @Date: 2018/10/9 20:45
 * @Description:商家控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/seller")
public class SellerController {

    //注入商家服务接口
    @Reference(timeout = 10000)
    private SellerService sellerService;

    /**
     * 商家注册
     * @param seller
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveSellerInfo(@RequestBody Seller seller){

        try {
            //使用BCrypt加密
            //创建BCrypt加密对象
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //加密
            String password  = passwordEncoder.encode(seller.getPassword());
            //把加密后的密码封装回seller对象
            seller.setPassword(password );
            sellerService.save(seller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
