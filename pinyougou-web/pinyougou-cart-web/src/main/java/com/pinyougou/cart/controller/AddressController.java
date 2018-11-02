package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：AddressController类
 * @Date: 2018/10/31 19:22
 * @Description:地址控制器
 * @Version：1.0
 */

@RestController
@RequestMapping("order")
public class AddressController {

    //引入地址服务
    @Reference(timeout = 10000)
    private AddressService addressService;

    /**
     * 根据登陆用户获取用户的收货地址
     * @param request
     * @return List<Address>
     */
    @GetMapping("/findAddressByUser")
    public List<Address> findAddressByUser(HttpServletRequest request) {

        //获取登陆用户的用户名
        String loginName = request.getRemoteUser();

        //根据登陆用户名查找该用户的收货地址
        List<Address> addressList = addressService.findAddressByUser(loginName);

        return addressList;
    }

    /**
     * 保存/修改 收货地址
     * @param address
     * @param request
     * @return Boolean
     */
    @PostMapping("saveOrUpdata")
    public Boolean saveAddress(@RequestBody Address address, HttpServletRequest request) {

        try {

            //获取登陆用户名
            String loginName = request.getRemoteUser();

            //把用户名封装到address对象
            address.setUserId(loginName);


            //获取收货地址的id
            Long id = address.getId();

            //判断是否存在该地址，
            if (id != null) {
                //修改地址
                //修改用户的收货地址
                addressService.update(address);
            }else {
                //新增地址
                //保存用户的收货地址
                addressService.save(address);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据id与用户名 删除收货地址
     * @param id
     * @return Boolean
     */
    @GetMapping("deleteAddress")
    public Boolean deleteAddress(Long id,HttpServletRequest request) {

        try {
            //获取当前登陆用户名
            String loginName = request.getRemoteUser();
            //根据id与用户名删除收货地址
            addressService.delete(id, loginName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
