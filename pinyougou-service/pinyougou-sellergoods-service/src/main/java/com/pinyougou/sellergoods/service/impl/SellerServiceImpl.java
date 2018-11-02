package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：SellerServiceImpl类
 * @Date: 2018/10/9 20:48
 * @Description:商家服务接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    /**
     * 添加方法
     *
     * @param seller
     */
    @Override
    public void save(Seller seller) {

        try {

            //设置审核状态
            seller.setStatus("0");
            //设置注册时间
            seller.setCreateTime(new Date());

            sellerMapper.insert(seller);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 修改方法
     *
     * @param seller
     */
    @Override
    public void update(Seller seller) {

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
    public Seller findOne(Serializable id) {

        return sellerMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部
     */
    @Override
    public List<Seller> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param seller
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(Seller seller, int page, int rows) {

        try {
            PageInfo<Seller> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    //创建示范对象
                    Example example = new Example(Seller.class);
                    //创建条件对象
                    Example.Criteria criteria = example.createCriteria();
                    //状态条件不等于空
                    if (seller != null && !StringUtils.isEmpty(seller.getStatus())) {
                        criteria.andEqualTo("status", seller.getStatus());
                    }
                    //公司不等于空
                    if (seller != null && !StringUtils.isEmpty(seller.getName())) {
                        criteria.andLike("name", "%" + seller.getName() + "%");
                    }
                    //店铺不等于空
                    if (seller != null && !StringUtils.isEmpty(seller.getNickName())) {
                        criteria.andLike("nickName", "%" + seller.getNickName() + "%");
                    }
                    //根据条件查询
                    sellerMapper.selectByExample(example);
                }
            });

            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 修改商家的状态（审核商家）
     *
     * @param sellerId
     * @param status
     */
    @Override
    public void updateStatus(String sellerId, String status) {

        try {

            //实例化商家对象
            Seller seller = new Seller();
            //封装条件sellerId和修改的值status
            seller.setSellerId(sellerId);
            seller.setStatus(status);

            //根据条件修改
            sellerMapper.updateByPrimaryKeySelective(seller);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
