package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：specificationController类
 * @Date: 2018/10/3 13:48
 * @Description:规格列表控制器
 * @Version：1.0
 */

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    //引用服务接口代理对象
    @Reference(timeout = 10000)
    private SpecificationService specificationService;

    /**
     * 根据条件分页查询规格列表
     * @return PageResult
     */
    @GetMapping("/findSpecification")
    public PageResult findSpecificationBySpecificationName(Specification specification,Integer page, Integer row){

        try {
            //前端是否传了规格名称过来，如果传了，就转码
            if (specification != null && StringUtils.isNoneBlank(specification.getSpecName())) {
                specification.setSpecName(new String(specification.getSpecName().getBytes("ISO8859-1"),"UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return specificationService.findByPage(specification, page, row);

    }

    /**
     * 查询所有
     * @return List<Map<String,Object>>
     */
    @GetMapping("/findSpecificationList")
    public List<Map<String,Object>> findSpecificationList() {

        return specificationService.findSpecificationList();
    }

    /**
     * 根据规格id查询规格选项
     * @param id
     * @return PageResult
     */
    @GetMapping("/findSpecificationOption")
    public List<SpecificationOption> findSpecificationOptionBySpecificationId(Long id) {

        return specificationService.findSpecificationOption(id);
    }

    /**
     * 保存规格信息
     * @param specification
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveSpecification(@RequestBody Specification specification){

        try {
            specificationService.save(specification);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 修改规格信息
     * @param specification
     * @return boolean
     */
    @PostMapping("/update")
    public boolean updateSpecification(@RequestBody Specification specification) {
        try {
            specificationService.update(specification);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据规格id删除规格信息
     * @param ids
     * @return boolean
     */
    @GetMapping("/delete")
    public boolean deleteSpecification(Long[] ids) {

        if (ids.length != 0) {
            try {
                specificationService.deleteAll(ids);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
