package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.ContentCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentCategoryController类
 * @Date: 2018/10/14 10:39
 * @Description:广告分类管理控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

    //注入服务层
    @Reference(timeout = 10000)
    private ContentCategoryService contentCategoryService;

    /**
     * 分页查询广告分类列表
     * @param page
     * @param row
     * @return PageResult
     */
    @RequestMapping("/finAllContentCategory")
    public PageResult finAllContentCategory(Integer page, Integer row) {

        return contentCategoryService.findByPage(null, page, row);
    }

    /**
     * 查询广告分类列表
     * @return List<ContentCategory>
     */
    @RequestMapping("/finAllCategory")
    public List<ContentCategory> findAllContentCategory(){

        try {
            return contentCategoryService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存广告分类
     * @param contentCategory
     * @return boolean
     */
    @RequestMapping("/saveContentCategory")
    public boolean saveContentCategory (@RequestBody ContentCategory contentCategory) {

        try {
            contentCategoryService.save(contentCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据id修改广告分类
     * @param contentCategory
     * @return boolean
     */
    @RequestMapping("/updateContentCategory")
    public boolean updateContentCategory(@RequestBody ContentCategory contentCategory) {

        try {
            contentCategoryService.update(contentCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据id删除广告分类
     * @param ids
     * @return boolean
     */
    @RequestMapping("/deleteContentCategory")
    public boolean deleteContentCategory(Long[] ids) {

        if (ids.length > 0) {
            try {
                contentCategoryService.deleteAll(ids);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return true;
        }

        return false;
    }

}
