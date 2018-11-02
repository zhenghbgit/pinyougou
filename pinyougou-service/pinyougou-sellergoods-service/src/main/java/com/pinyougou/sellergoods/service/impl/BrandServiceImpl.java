package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：BrandServiceImpl类
 * @Date: 2018/9/28 15:58
 * @Description:品牌服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.BrandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    //注入数据访问接口代理对象
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 添加方法
     *
     * @param brand
     */
    @Override
    public void save(Brand brand) {
        try {
            brandMapper.insert(brand);
        }catch (Exception e){
            new RuntimeException("添加失败!");
        }

    }

    /**
     * 修改方法
     *
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        try {
            brandMapper.updateByPrimaryKey(brand);
        } catch (Exception e) {
            throw new RuntimeException("修改失败！");
        }
    }

    /**
     * 根据主键id删除
     *
     * @param id
     */
    @Override
    public void delete(Serializable id) {

    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void deleteAll(Serializable[] ids) {

        //创建示范对象
        Example example = new Example(Brand.class);
        //创建条件对象
        Example.Criteria criteria = example.createCriteria();
        //添加in条件
        criteria.andIn("id", Arrays.asList(ids));
        //根据条件删除
        brandMapper.deleteByExample(example);
    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Brand findOne(Serializable id) {
        return null;
    }

    /**
     * 查询所有品牌
     *
     * @return List<Brand>
     */
    @Override
    public List<Brand> findAll() {

        return brandMapper.selectAll();
    }

    /**
     * 多条件分页查询
     *
     * @param brand
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(Brand brand, int page, int rows) {

        PageInfo<Brand> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                brandMapper.findBrandByTerm(brand);
            }
        });

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 查询所有品牌信息(Id和Name)
     *
     * @return List<Map < String ,   S tring>>
     */
    @Override
    public List<Map<String, Object>> findAllIdAndName() {

        return brandMapper.findAllIdAndName();
    }

}
