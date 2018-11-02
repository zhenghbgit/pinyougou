package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemCatServiceImp类
 * @Date: 2018/10/8 19:01
 * @Description:分类管理服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.ItemCatService")
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    //注入数据访问层
    @Autowired
    private ItemCatMapper itemCatMapper;


    /**
     * 添加方法
     *
     * @param itemCat
     */
    @Override
    public void save(ItemCat itemCat) {
        try {
            itemCatMapper.insert(itemCat);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 修改方法
     *
     * @param itemCat
     */
    @Override
    public void update(ItemCat itemCat) {

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

        try {

            //查询下级目录列表
            List<ItemCat> itemCatList = findItemCatByParentId(ids);

            //删除方法
            //创建示范对象
            Example example = new Example(ItemCat.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //in条件
            criteria.andIn("id", Arrays.asList(ids));
            //根据条件删除
            itemCatMapper.deleteByExample(example);

            //用于存储下级目录列表id
            List<Serializable> serializableList = new ArrayList<>();

            //如果查询有结果，则继续查询
            if (itemCatList.size() != 0) {

                //遍历，获取下级单元的id，用serializableList集合存储
                for (ItemCat itemCat : itemCatList) {
                    serializableList.add(itemCat.getId());
                }

                //将serializableList集合转化为数组
                Serializable[] serializables = serializableList.toArray(ids);

                //继续调用删除方法
                deleteAll(serializables);

            }
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
    public ItemCat findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<ItemCat> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param itemCat
     * @param page
     * @param rows
     */
    @Override
    public List<ItemCat> findByPage(ItemCat itemCat, int page, int rows) {
        return null;
    }

    /**
     * 查询分类列表（最上级）
     *
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {

        //实例化itemcat对象，存储parentId
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);

        //根据对象的值查询分类管理列表
        List<ItemCat> itemCatList = itemCatMapper.select(itemCat);

        return itemCatList;
    }

    /**
     * 根据数组parentId查询分类列表
     * @param parentId
     * @return List<ItemCat>
     */
    public List<ItemCat>  findItemCatByParentId(Serializable[] parentId) {

        //创建示范对象
        Example example = new Example(ItemCat.class);
        //创建条件对象
        Example.Criteria criteria = example.createCriteria();
        //in条件
        criteria.andIn("parentId", Arrays.asList(parentId));
        //查询
        List<ItemCat> itemCatList = itemCatMapper.selectByExample(example);

        return itemCatList;
    }

}
