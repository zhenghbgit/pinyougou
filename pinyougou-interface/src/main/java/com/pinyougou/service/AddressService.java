package com.pinyougou.service;

import com.pinyougou.pojo.Address;

import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：AddressService类
 * @Date: 2018/9/29 17:25
 * @Description:地址服务接口
 * @Version：1.0
 */

public interface AddressService {

    /**
     * 添加方法
     */
    void save(Address address);

    /**
     * 修改方法
     */
    void update(Address address);

    /**
     * 根据主键id删除
     */
    void delete(Serializable id);

    /**
     * 批量删除
     */
    void deleteAll(Serializable[] ids);

    /**
     * 根据主键id查询
     */
    Address findOne(Serializable id);

    /**
     * 查询全部
     */
    List<Address> findAll();

    /**
     * 多条件分页查询
     */
    List<Address> findByPage(Address address, int page, int rows);

    /**
     * 根据登陆用户获取用户的收货地址
     * @param loginName
     * @return List<Address>
     */
    List<Address> findAddressByUser(String loginName);

    /**
     * 根据id与用户名删除收货地址
     * @param id
     * @param loginName
     */
    void delete(Long id, String loginName);
}