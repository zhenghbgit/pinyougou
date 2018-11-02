package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemCatController类
 * @Date: 2018/10/8 18:52
 * @Description:分类管理控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    //注入服务层
    @Reference(timeout = 10000)
    private ItemCatService itemCatService;

    /**
     * 根据查询分类列表(最上级)
     * @param parentId
     * @return List<ItemCat>
     */
    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(Long parentId) {

        return itemCatService.findItemCatByParentId(parentId);
    }

    /**
     * 保存分类模板信息
     * @param itemCat
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveItemCat(@RequestBody ItemCat itemCat){

        try {
            itemCatService.save(itemCat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    @GetMapping("/delete")
    public boolean deleteItemCat(Long[] ids){

        try {
            itemCatService.deleteAll(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
