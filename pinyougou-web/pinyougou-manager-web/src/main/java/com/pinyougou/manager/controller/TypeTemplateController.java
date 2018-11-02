package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：TypeTemplateController类
 * @Date: 2018/10/5 11:34
 * @Description:模板控制器
 * @Version：1.0
 */


@RestController
@RequestMapping("/TypeTemplate")
public class TypeTemplateController {

    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    /**
     * 根据条件查询模板列表
     * @param page
     * @param row
     * @return PageResult
     */
    @GetMapping("/findTypeTemplate")
    public PageResult findTypeTemplateByTypeTemplateName(TypeTemplate typeTemplate, Integer page, Integer row) {

        try {
            if (typeTemplate != null && StringUtils.isNoneBlank(typeTemplate.getName())) {
                typeTemplate.setName(new String(typeTemplate.getName().getBytes("ISO8859-1"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return typeTemplateService.findByPage(typeTemplate, page, row);
    }

    /**
     * 查询所有模板列表
     * @return List<Map<String,Object>>
     */
    @GetMapping("/findTypeTemplateList")
    public List<Map<String,Object>> findTypeTemplateList() {

        return typeTemplateService.findTypeTemplateList();

    }

    /**
     * 保存模板信息
     * @param typeTemplate
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveTypeTemplate(@RequestBody TypeTemplate typeTemplate) {

        try {
            typeTemplateService.save(typeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据模板id修改模板信息
     * @param typeTemplate
     * @return boolean
     */
    @PostMapping("/update")
    public boolean updateTypeTemplate(@RequestBody TypeTemplate typeTemplate) {

        try {
            typeTemplateService.update(typeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据模板id删除模板信息
     * @param ids
     * @return boolean
     */
    @GetMapping("/delete")
    public boolean deleteTypeTemplateById(Long[] ids) {

        if (ids.length != 0) {
            try {
                typeTemplateService.deleteAll(ids);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


}
