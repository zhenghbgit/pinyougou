package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：GoodsServiceImpl类
 * @Date: 2018/10/10 16:19
 * @Description:商品服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {

    //注入商品持久层
    @Autowired
    private GoodsMapper goodsMapper;
    //注入商品描述持久层
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    //注入商品类型持久层
    @Autowired
    private ItemMapper itemMapper;
    //注入商品类型分类持久层
    @Autowired
    private ItemCatMapper itemCatMapper;
    //注入品牌持久层
    @Autowired
    private BrandMapper brandMapper;
    //注入商家持久层
    @Autowired
    private SellerMapper sellerMapper;


    /**
     * 添加方法
     *
     * @param goods
     */
    @Override
    public void save(Goods goods) {

        try {
            //保存商品数据
            goodsMapper.insert(goods);

            //获取商品描述对象
            GoodsDesc goodsDesc = goods.getGoodsDesc();
            //设置商品描述对象主键
            goodsDesc.setGoodsId(goods.getId());
            //保存商品描述数据
            goodsDescMapper.insert(goodsDesc);

            //是否启用规格
            if ("1".equals(goods.getIsEnableSpec())) {
                //保存商品类型数据
                for (Item item : goods.getItems()) {

                    //拼接标题
                    StringBuilder sbTitle = new StringBuilder();
                    sbTitle.append(goods.getGoodsName());

                    //把json字符串格式转化为map集合
                    Map<String, Object> mapSpec = JSON.parseObject(item.getSpec());
                    //把map集合转化为Collection集合,再进行遍历获取值
                    for (Object value : mapSpec.values()) {
                        //获取、拼接规格
                        sbTitle.append(" " + value);
                    }

                    //标题
                    item.setTitle(sbTitle.toString());

                    //封装Item数据
                    setItem(item, goods);

                    //插入商品类型数据
                    itemMapper.insert(item);
                }
            } else {

                //创建SKU商品类型实体类对象
                Item item = new Item();
                //标题
                item.setTitle(goods.getGoodsName());
                //价格
                item.setPrice(new BigDecimal("0"));
                //库存
                item.setNum(999);
                //默认选中该商品
                item.setIsDefault("1");
                //商品规格
                item.setSpec("{}");
                //封装Item共同数据
                setItem(item, goods);

                //插入商品类型数据
                itemMapper.insertSelective(item);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 封装Item共同数据
     * @param item
     * @param goods
     */
    public void setItem(Item item,Goods goods){

        //把json数组格式转化为List-Map集合
        List<Map> imgList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        //判断该商品是否有图片
        if (imgList != null && imgList.size() > 0) {
            //取第一张图片
            item.setImage((String) imgList.get(0).get("url"));
        }

        //设置三级分类
        item.setCategoryid(goods.getCategory3Id());
        //设置状态码
        item.setStatus("1");
        //设置创建时间
        item.setCreateTime(new Date());
        //设置更新时间
        item.setUpdateTime(item.getCreateTime());
        //设置商品id
        item.setGoodsId(goods.getId());
        //设置商家id
        item.setSellerId(goods.getSellerId());
        //设置分类名称
        item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName());
        //设置品牌
        item.setBrand(brandMapper.selectByPrimaryKey(goods.getBrandId()).getName());
        //设置商家店铺名称
        item.setSeller(sellerMapper.selectByPrimaryKey(goods.getSellerId()).getNickName());

    }

    /**
     * 修改方法
     *
     * @param goods
     */
    @Override
    public void update(Goods goods) {

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

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Goods> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param goods
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {

        try {

            //分装分页数据
            PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    goodsMapper.findAllGoods(goods);
                }
            });

            //查询分类目录
            for (Map<String, Object> map : pageInfo.getList()) {

                //查询一级分类
                ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
                //获取一级分类的名称，存储到map集合
                map.put("category1Name", itemCat1 != null ? itemCat1.getName() : "");

                //查询二级分类
                ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
                //查询二级分类的名称，存储到map集合
                map.put("category2Name", itemCat2 != null ? itemCat2.getName() : "");

                //查询三级目录
                ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
                //获取三级分类的名称，存储到map集合
                map.put("category3Name", itemCat3 != null ? itemCat3.getName() : "");

            }

            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改商品状态
     *
     * @param status
     * @param ids
     */
    @Override
    public void updataStatus(Long[] ids,String status,String typeName) {

        try {

            //判断是否要上架，上架需要审批通过
            if ("is_marketable".equals(typeName) && "1".equals(status)) {

                //根据商品ids查询审批通过的商品
                List<Goods> goodsList = goodsMapper.findGoodsByIds(ids);
                //如果查到的商品数与ids的数量一样，则说明传过来的id已经都审批了
                if (goodsList.size() == ids.length) {
                    goodsMapper.updataStatus(ids,status,typeName);
                }else {
                    throw new RuntimeException("上架的商品中还有未审核商品！");
                }

            }else {
                goodsMapper.updataStatus(ids,status,typeName);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据商品id查询商品详情信息
     *
     * @param goodsId
     * @return Map<String , Object>
     */
    @Override
    public Map<String, Object> findGoodsByGoodsId(Long goodsId) {

        //创建Map集合封装数据
        Map<String, Object> map = new HashMap<>();

        //根据id查询商品goods
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);

        //根据id查询商品goodsDesc
        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

        //根据id查询商品item
        //创建示范对象
        Example example = new Example(Item.class);
        //创建条件对象
        Example.Criteria criteria = example.createCriteria();
        //条件
        criteria.andEqualTo("goodsId", goodsId);
        criteria.andEqualTo("status", "1");
        //按默认降序进行查询
        example.orderBy("isDefault").desc();
        //添加条件查询
        List<Item> itemList = itemMapper.selectByExample(example);

        map.put("itemList", JSON.toJSONString(itemList));

        //获取分类名称
        if (goods.getGoodsName() != null && goods.getCategory3Id() != null) {
            //获取三级分类名称
            ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
            //获取二级分类名称
            ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
            //获取一级分类名称
            ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());

            map.put("itemCat1", itemCat1.getName());
            map.put("itemCat2", itemCat2.getName());
            map.put("itemCat3", itemCat3.getName());
        }

        map.put("goods", goods);
        map.put("goodsDesc", goodsDesc);

        return map;
    }

    /**
     * 根据商品id查询商品SKU
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<Item> findItemByGoodIds(Long[] goodsIds) {

        //创建示例对象
        Example example = new Example(Item.class);
        //根据示例对象创建条件对象
        Example.Criteria criteria = example.createCriteria();
        //添加in条件
        criteria.andIn("goodsId", Arrays.asList(goodsIds));
        //查询
        List<Item> itemList = itemMapper.selectByExample(example);

        return itemList;
    }
}
