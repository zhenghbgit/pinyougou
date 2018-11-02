package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SearchController类
 * @Date: 2018/10/18 9:03
 * @Description:搜索控制层
 * @Version：1.0
 */

@RestController
public class SearchController {

    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;

    @PostMapping("/Search")
    public Map<String,Object> search(@RequestBody Map<String,Object> mapParam) {

        return itemSearchService.searchItem(mapParam);
    }

}
