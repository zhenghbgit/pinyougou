package com.pinyougou.shop.service;

import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UserDetailsService类
 * @Date: 2018/10/10 0:59
 * @Description:自定义认证服务
 * @Version：1.0
 */

public class UserDetailsServiceImpl implements UserDetailsService {

    //注入商家服务接口
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //创建角色集合
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        //添加角色
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //根据用户名查询用户
        Seller seller = sellerService.findOne(username);

        //判断商家是否为空并且商家已审核
        if (seller != null && "1".equals(seller.getStatus())) {

            //返回用户信息对象
            return new User(username, seller.getPassword(), grantedAuthorityList);
        }

        //没有找到用户
        return null;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
