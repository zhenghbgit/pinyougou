package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SellerController类
 * @Date: 2018/10/9 21:22
 * @Description:商家审核控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;

    /**
     * 根据条件查询未审核商家列表
     * @param seller
     * @param page
     * @param row
     * @return PageResult
     */
    @GetMapping("/findCheckedSeller")
    public PageResult findCheckedSeller(Seller seller,Integer page, Integer row){

        try {
            if (seller != null && StringUtils.isNoneBlank(seller.getName())) {
                seller.setName(new String(seller.getName().getBytes("ISO8859-1"), "UTF-8"));
            }
            if (seller != null && StringUtils.isNoneBlank(seller.getNickName())) {
                seller.setNickName(new String(seller.getNickName().getBytes("ISO8859-1"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sellerService.findByPage(seller, page, row);
    }

    /**
     * 修改未审核商家的状态(审核商家)
     * @param sellerId
     * @param status
     * @return
     */
    @GetMapping("/updateStatus")
    public boolean updateStatus(String sellerId, String status) {

        try {
            sellerService.updateStatus(sellerId, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
