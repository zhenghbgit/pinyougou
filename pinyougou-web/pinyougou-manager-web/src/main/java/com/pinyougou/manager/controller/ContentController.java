package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Content;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentController类
 * @Date: 2018/10/14 14:45
 * @Description:广告管理控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/content")
public class ContentController {

    //引入广告服务层
    @Reference(timeout = 10000)
    private ContentService contentService;

    /**
     * 分页查询广告
     * @param page
     * @param row
     * @return PageResult
     */
    @RequestMapping("/findContentPage")
    public PageResult findContentPage(Integer page,Integer row) {

        return contentService.findByPage(null, page, row);
    }

    /**
     * 保存广告信息
     * @param content
     * @return boolean
     */
    @RequestMapping("/saveContent")
    public boolean saveContent(@RequestBody Content content) {

        try {
            contentService.save(content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据id修改广告信息
     * @param content
     * @return boolean
     */
    @RequestMapping("/updateContent")
    public boolean updateContent(@RequestBody Content content) {

        try {
            contentService.update(content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据id删除广告信息
     * @param ids
     * @return
     */
    @RequestMapping("/deleteContent")
    public boolean deleteContent(Long[] ids){

        if (ids.length > 0) {
            try {
                contentService.deleteAll(ids);
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
