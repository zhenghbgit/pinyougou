package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
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
 * @Title：TypeTemplateServiceImpl类
 * @Date: 2018/10/5 11:35
 * @Description:模板服务接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    /*注入模板持久层接口*/
    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    /*注入规格选项持久层接口*/
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 添加方法
     *
     * @param typeTemplate
     */
    @Override
    public void save(TypeTemplate typeTemplate) {

        try {
            typeTemplateMapper.insert(typeTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 修改方法
     *
     * @param typeTemplate
     */
    @Override
    public void update(TypeTemplate typeTemplate) {

        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
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
        Example example = new Example(TypeTemplate.class);
        //创建条件对象
        Example.Criteria criteria = example.createCriteria();
        //添加in条件
        criteria.andIn("id", Arrays.asList(ids));
        //根据条件删除
        typeTemplateMapper.deleteByExample(example);

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public TypeTemplate findOne(Serializable id) {

        try {

            return typeTemplateMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 查询全部
     */
    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param typeTemplate
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {

        PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                typeTemplateMapper.findTypeTemplateByTypeTemplateName(typeTemplate);
            }
        });

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 查询所有模板列表
     *
     * @return List<Map < String , Object>>
     */
    @Override
    public List<Map<String, Object>> findTypeTemplateList() {

        return typeTemplateMapper.findTypeTemplateList();
    }

    /**
     * 根据模板id查找模板（获取规格与规格选项）
     *
     * @param id
     * @return List<Map>
     */
    @Override
    public List<Map> typeTemplateService(Long id) {

        try {
            //根据id查找模板
            TypeTemplate typeTemplate = findOne(id);
            //获取规格列表 specIds = "[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]"
            String specIds = typeTemplate.getSpecIds();
            //将规格列表json格式转化为map对象 specList = [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
            List<Map> specList = JSON.parseArray(specIds, Map.class);

            //遍历specList集合
            for (Map map : specList) {

                //获取规格id
                Integer specId  = (Integer) map.get("id");
                //创建规格选项实体类对象
                SpecificationOption specOption = new SpecificationOption();
                //封装规格id
                specOption.setSpecId(specId.longValue());
                //根据规格id查找规格选项
                List<SpecificationOption> specOptionList = specificationOptionMapper.select(specOption);

                //把查找到的规格选项放到map集合
                map.put("options", specOptionList);        //specList = [{"id":27,"text":"网络","options":[{}]},{"id":32,"text":"机身内存"}]

            }

            return specList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
