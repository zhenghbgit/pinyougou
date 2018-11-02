package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：BrandController类
 * @Date: 2018/9/28 16:00
 * @Description:品牌控制器类
 * @Version：1.0
 */

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference(timeout = 10000)
    private BrandService brandService;

    /**
     * 分页查询所有品牌
     * @return PageResult
     */
    @GetMapping("/findByPage")
    public PageResult findByPage(Brand brand,Integer page, Integer row){

        try {
            //判断前端传过来的品牌信息是否是姓名，是否为空，如果不是，就转码
            if (brand != null && StringUtils.isNoneBlank(brand.getName())) {
                brand.setName(new String(brand.getName().getBytes("ISO8859-1"),"UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return brandService.findByPage(brand, page, row);
    }

    /**
     * 查询所有品牌信息
     * @return List<Map<String,String>>
     */
    @GetMapping("/findBrandList")
    public List<Map<String,Object>> findBrandList() {

        return brandService.findAllIdAndName();
    }

    /**
     * 保存品牌信息
     * @param brand
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveBrand(@RequestBody Brand brand){
        try {
            brandService.save(brand);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据品牌id修改品牌信息
     * @param brand
     * @return boolean
     */
    @PostMapping("/update")
    public boolean updateBrand(@RequestBody Brand brand) {
        try {
            brandService.update(brand);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据品牌id删除品牌信息
     * @param ids
     * @return boolean
     */
    @GetMapping("/deleteBrandById")
    public boolean deleteBrandById(Integer[] ids) {

        if (ids.length != 0) {
            try {
                brandService.deleteAll(ids);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
