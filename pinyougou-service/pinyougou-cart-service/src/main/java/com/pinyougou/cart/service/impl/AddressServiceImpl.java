package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：AddressServiceImpl类
 * @Date: 2018/10/31 19:25
 * @Description:地址服务接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.AddressService")
@Transactional
public class AddressServiceImpl implements AddressService{

    //注入地址持久层
    @Autowired
    private AddressMapper addressMapper;

    /**
     * 添加方法
     *
     * @param address
     */
    @Override
    public void save(Address address) {

        try {

            //获取

            //保存用户地址
            addressMapper.insertSelective(address);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改方法
     *
     * @param address
     */
    @Override
    public void update(Address address) {

        try {
            addressMapper.updateByPrimaryKey(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Address findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Address> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param address
     * @param page
     * @param rows
     */
    @Override
    public List<Address> findByPage(Address address, int page, int rows) {
        return null;
    }

    /**
     * 根据登陆用户获取用户的收货地址
     *
     * @param loginName
     * @return List<Address>
     */
    @Override
    public List<Address> findAddressByUser(String loginName) {

        /** 创建地址对象封装查询条件 */
        Address address = new Address();
        address.setUserId(loginName);

        return addressMapper.select(address);
    }

    /**
     * 根据id与用户名删除收货地址
     *
     * @param id
     * @param loginName
     */
    @Override
    public void delete(Long id, String loginName) {

        try {
            //创建示范对象
            Example example = new Example(Address.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //条件
            criteria.andEqualTo("id", id);
            criteria.andEqualTo("userId", loginName);
            //执行，根据条件删除收货地址
            addressMapper.deleteByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
