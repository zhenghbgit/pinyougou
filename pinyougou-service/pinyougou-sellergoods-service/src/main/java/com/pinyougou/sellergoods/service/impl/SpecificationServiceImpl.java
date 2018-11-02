package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationService;
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
 * @Title：SpecificationServiceImpl类
 * @Date: 2018/10/3 15:10
 * @Description:规格接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.SpecificationService")
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    //注入Specification持久层
    @Autowired
    private SpecificationMapper specificationMapper;
    //注入SpecificationOption持久层
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 添加方法
     *
     * @param specification
     */
    @Override
    public void save(Specification specification) {

        try {
            //添加规格数据
            specificationMapper.insert(specification);
            //添加规格选项数据
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 修改方法
     *
     * @param specification
     */
    @Override
    public void update(Specification specification) {

        try {
            //根据规格id修改规格
            specificationMapper.updateByPrimaryKey(specification);

            //创建选项规格实例对象
            SpecificationOption specificationOption = new SpecificationOption();
            specificationOption.setSpecId(specification.getId());

            //删除该规格的全部选项
            specificationOptionMapper.delete(specificationOption);
            //插入新的规格选项
            specificationOptionMapper.save(specification);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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

        //把数组转化为集合
        List<Serializable> list = Arrays.asList(ids);

        try {
            //删除规格选项
            //创建示范对象
            Example example = new Example(SpecificationOption.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //添加in条件
            criteria.andIn("specId", list);
            //根据条件删除
            specificationOptionMapper.deleteByExample(example);

            //删除规格
            //创建示范对象
            Example example1 = new Example(Specification.class);
            //创建条件对象
            Example.Criteria criteria1 = example1.createCriteria();
            //添加in条件
            criteria1.andIn("id", list);
            //根据条件删除
            specificationMapper.deleteByExample(example1);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Specification findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Specification> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param specification
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(Specification specification, int page, int rows) {

        PageInfo<Object> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                specificationMapper.findSpecificationBySpecificationName(specification);
            }
        });

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 根据规格id查询规格选项
     *
     * @param id
     * @return List<SpecificationOption>
     */
    @Override
    public List<SpecificationOption> findSpecificationOption(Long id) {

        try {
            //实例化规格选项对象
            SpecificationOption specificationOption = new SpecificationOption();
            //把规格id给规格选项
            specificationOption.setSpecId(id);

            //根据条件查询
            return specificationOptionMapper.select(specificationOption);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询所有规格信息
     *
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> findSpecificationList() {

        return specificationMapper.findSpecificationList();
    }
}
