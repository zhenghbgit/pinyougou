package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import com.pinyougou.util.HttpClientUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UserServiceImpl类
 * @Date: 2018/10/26 17:42
 * @Description:用户服务接口实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    //短信服务请求地址
    @Value("${sms.smsURL}")
    private String smsURL;
    //短信签名
    @Value("${sms.signName}")
    private String signName;
    //短信模板
    @Value("${sms.templateCode}")
    private String templateCode;

    /**
     * 添加方法
     *
     * @param user
     */
    @Override
    public void save(User user) {

        try {

            //用户创建时间
            user.setCreated(new Date());
            //修改用户时间
            user.setUpdated(new Date());
            //使用MD5加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));

            userMapper.insertSelective(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改方法
     *
     * @param user
     */
    @Override
    public void update(User user) {

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
    public User findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<User> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param user
     * @param page
     * @param rows
     */
    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNum
     */
    @Override
    public boolean sendSMS(String phoneNum) {

        try {
            //生成六位数验证码
            String codeStr = UUID.randomUUID().toString();
            //将生成的UUID去掉-
            String replace = codeStr.replaceAll("-", "");
            //去掉UUID中的字母
            String rep = replace.replaceAll("[a-z|A-Z]", "");
            //截取六位数
            String code = rep.substring(0, 6);
            //验证码
            System.err.println("验证码：" + code);

            //调用短信接口，发送短信
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);

            //封装参数
            Map<String, String> paramMap = new HashMap<>();
            //手机号码
            paramMap.put("phoneNum", phoneNum);
            //短信签名
            paramMap.put("signName", signName);
            //短信模板
            paramMap.put("templateCode", templateCode);
            //验证码
            paramMap.put("templateParam", "{'number' : " + code + "}");

            //发送Post请求，请求发送短信
            String success = httpClientUtils.sendPost(smsURL, paramMap);
            //将Json字符串转化为Map
            Map<String,Object> map = JSON.parseObject(success, Map.class);

            //将验证码存入Redis,存储时间为90秒
            redisTemplate.boundValueOps(phoneNum).set(code,90, TimeUnit.SECONDS);

            return (Boolean) map.get("success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     * @return boolean
     */
    @Override
    public boolean checkSMSCode(String phone, String code) {

        try {
            //从redis中获取验证码
            String sysCode = (String) redisTemplate.boundValueOps(phone).get();
            //验证
            return StringUtils.isNoneBlank(sysCode) && code.equals(sysCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
