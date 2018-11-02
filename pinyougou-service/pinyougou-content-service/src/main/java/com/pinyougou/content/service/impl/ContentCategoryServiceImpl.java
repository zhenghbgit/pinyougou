package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentCategoryMapper;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ContentCategoryServiceImp类
 * @Date: 2018/10/14 10:41
 * @Description:广告类型管理服务实现类
 * @Version：1.0
 */

@Service(interfaceName = "com.pinyougou.service.ContentCategoryService")
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

    /*注入广告管理持久层接口*/
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    /**
     * 添加方法
     *
     * @param contentCategory
     */
    @Override
    public void save(ContentCategory contentCategory) {

        try {
            contentCategoryMapper.insert(contentCategory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改方法
     *
     * @param contentCategory
     */
    @Override
    public void update(ContentCategory contentCategory) {

        try {
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
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
            contentCategoryMapper.deleteContentCategoryById(ids);
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
    public ContentCategory findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<ContentCategory> findAll() {

        try {
            return contentCategoryMapper.selectAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 多条件分页查询
     *
     * @param contentCategory
     * @param page
     * @param rows
     */
    @Override
    public PageResult findByPage(ContentCategory contentCategory, int page, int rows) {

        try {
            PageInfo<ContentCategory> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    contentCategoryMapper.selectAll();
                }
            });

            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
