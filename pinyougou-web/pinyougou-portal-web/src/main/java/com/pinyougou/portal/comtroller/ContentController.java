package com.pinyougou.portal.comtroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Content;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentController类
 * @Date: 2018/10/15 16:16
 * @Description:广告控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000)
    private ContentService contentService;

    /**
     * 根据广告分类查询广告
     * @param categoryId
     * @return List<Content>
     */
    @RequestMapping("/findContentByCategoryId")
    public List<Content> findContentByCategoryId(Long categoryId) {

        return contentService.findContentByCategoryId(categoryId);
    }

}
