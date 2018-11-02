package com.pinyougou.service;

import com.pinyougou.pojo.User;
import java.util.List;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UserService类
 * @Date: 2018/9/29 17:33
 * @Description:用户服务接口
 * @Version：1.0
 */

public interface UserService {

	/** 添加方法 */
	void save(User user);

	/** 修改方法 */
	void update(User user);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	User findOne(Serializable id);

	/** 查询全部 */
	List<User> findAll();

	/** 多条件分页查询 */
	List<User> findByPage(User user, int page, int rows);

	/**
	 * 发送短信验证码
	 * @param phoneNum
	 * @return boolean
	 */
	boolean sendSMS(String phoneNum);

	/**
	 * 验证验证码
	 * @param phone
	 * @param code
	 * @return boolean
	 */
    boolean checkSMSCode(String phone, String code);
}