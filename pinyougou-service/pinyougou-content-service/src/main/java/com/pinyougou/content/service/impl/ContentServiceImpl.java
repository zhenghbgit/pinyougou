package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.Content;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentServiceImp类
 * @Date: 2018/10/14 14:47
 * @Description:广告管理服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {

    //注入广告管理持久层
    @Autowired
    private ContentMapper contentMapper;
    //注入redisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加方法
     *
     * @param content
     */
    @Override
    public void save(Content content) {

        try {
            contentMapper.insert(content);
            //清除redis缓存中的content1
            redisTemplate.delete("content1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改方法
     *
     * @param content
     */
    @Override
    public void update(Content content) {

        try {
            contentMapper.updateByPrimaryKey(content);
            //清除redis缓存中的content1
            redisTemplate.delete("content1");
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

        try {
            //创建示范对象
            Example example = new Example(Content.class);
            //创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //创建in条件
            criteria.andIn("id", Arrays.asList(ids));
            //根据条件删除
            contentMapper.deleteByExample(example);
            //清除redis缓存中的content1
            redisTemplate.delete("content1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Content findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Content> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param content
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(Content content, int page, int rows) {

        PageInfo<Content> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                contentMapper.selectAll();
            }
        });

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 根据广告分类查询广告
     *
     * @param categoryId
     * @return List<Content>
     */
    @Override
    public List<Content> findContentByCategoryId(Long categoryId) {

        List<Content> content1List = null;

        try {
            //从redis中取出content1的数据
            content1List = (List<Content>) redisTemplate.boundValueOps("content1").get();

            //判断redis里是否存在数据 存在数据直接返回数据
            if (content1List != null && content1List.size() > 0) {

                return content1List;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {

            //从数据库中查询数据
            //方法一:
            /*// 创建示范对象
            Example example = new Example(Content.class);
            // 创建查询条件对象
            Example.Criteria criteria = example.createCriteria();
            // 添加等于条件 category_id = categoryId
            criteria.andEqualTo("categoryId", categoryId);
            // 添加等于条件 status = 1
            criteria.andEqualTo("status", "1");
            // 排序(升序) order by sort_order asc
            example.orderBy("sortOrder").asc();*/
            //方法二：
            content1List = contentMapper.findContentByCategoryId(categoryId);

            //把数据存到redis缓存
            redisTemplate.boundValueOps("content1").set(content1List);

            return content1List;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
