package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：GoodsController类
 * @Date: 2018/10/13 20:18
 * @Description:商品审核控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    /**
     * 根据条件查询商品列表
     * @param page
     * @param row
     * @param goods
     * @return PageResult
     */
    @RequestMapping("/findPageGoods")
    public PageResult findPageGoods(Integer page, Integer row, Goods goods) {

        try {

            //添加搜索条件
            goods.setAuditStatus("0");  //未审核

            //搜索条件转码
            if (goods.getGoodsName() != null && StringUtils.isNoneEmpty(goods.getGoodsName())) {
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return goodsService.findByPage(goods, page, row);
    }

    /**
     * 修改商品的状态
     * @param status
     * @return boolean
     */
    @RequestMapping("/updateStatus")
    public boolean updateStatus(Long[] ids,String status,String typeName) {

        try {
            goodsService.updataStatus(ids,status,typeName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
