package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：TypeTemplateController类
 * @Date: 2018/10/11 14:39
 * @Description:模板控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/TypeTemplate")
public class TypeTemplateController {

    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    /**
     * 根据模板id查找模板（获取品牌）
     * @param id
     * @return TypeTemplate
     */
    @RequestMapping("/findTypeTemplate")
    public TypeTemplate findTypeTemplate(Long id) {

        try {
            return typeTemplateService.findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据模板id查找模板（获取规格与规格选项）
     * @param id
     * @return List<Map>
     */
    @RequestMapping("/findSpecOptionByTypeTemplateId")
    public List<Map> findSpecOptionByTypeTemplateId(Long id) {

        try {

            return typeTemplateService.typeTemplateService(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
