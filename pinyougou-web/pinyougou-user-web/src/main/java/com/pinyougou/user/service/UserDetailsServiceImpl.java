package com.pinyougou.user.service;

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
 * @Title：UserDetailsServiceImpl类
 * @Date: 2018/10/27 21:01
 * @Description:用户认证服务类
 * @Version：1.0
 */

public class UserDetailsServiceImpl implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //创建用户(角色)权限集合
        List<GrantedAuthority> authorityList = new ArrayList<>();
        //添加权限
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(username, "", authorityList);
    }
}
