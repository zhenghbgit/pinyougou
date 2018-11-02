package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemController类
 * @Date: 2018/10/22 19:34
 * @Description:商品详细控制器
 * @Version：1.0
 */

@Controller
public class ItemController {

    //引入商品服务
    @Reference(timeout = 10000)
    private GoodsService goodsService;

    /**
     * 根据商品id查询商品信息
     * @param goodsId
     * @param model
     * @return String
     */
    //item.pinyougou.com/149187842867997.html
    @GetMapping("/{goodsId}")
    private String getGoods(@PathVariable("goodsId") Long goodsId, Model model) {

        //根据id查询商品
        Map<String,Object> modelDate = goodsService.findGoodsByGoodsId(goodsId);

        //把数据添加到数据模型
        model.addAllAttributes(modelDate);

        //上面一条语句等价于下面两条语句
        //model.addAttribute("goodsDesc", modelDate.get("goodsDesc"));
        //model.addAttribute("goods", modelDate.get("goods"));

        //返回的是FreeMarker模板页面
        return "item";
    }

}
