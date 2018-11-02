package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.UnsupportedEncodingException;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：GoodsController类
 * @Date: 2018/10/10 16:13
 * @Description:商品管理控制层
 * @Version：1.0
 */

@RestController
@RequestMapping("/goods")
public class GoodsController {

    //注入商品服务层接口
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    //注入jmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;
    //注入消息模式(上架创建索引)
    @Autowired
    private Destination solrQueue;
    //注入消息模式（下架删除索引）
    @Autowired
    private Destination solrDeleteQueue;
    //注入消息模式（商品详情页面生成）
    @Autowired
    private Destination pageTopic;
    //注入消息模式（商品详情页面删除）
    @Autowired
    private Destination pageDeleteTopic;

    /**
     * 保存商品信息
     *
     * @param goods
     * @return boolean
     */
    @PostMapping("/save")
    public boolean saveGoods(@RequestBody Goods goods) {

        try {
            //获取登陆用户的用户名
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //封装用户名
            goods.setSellerId(userName);
            //设置商品状态
            goods.setAuditStatus("0");
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据条件查询商品列表
     *
     * @param page
     * @param row
     * @param goods
     * @return PageResult
     */
    @RequestMapping("/findByPage")
    public PageResult findGoods(Integer page, Integer row, Goods goods) {

        try {
            //获取商家用户名
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

            //把商家id给goods
            goods.setSellerId(sellerId);

            //搜索条件转码
            if (goods != null && StringUtils.isNoneEmpty(goods.getGoodsName())) {
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"), "UTF-8"));
            }

            return goodsService.findByPage(goods, page, row);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改商品的状态（上架、下架）
     *
     * @return boolean
     */
    @RequestMapping("/updateStatus")
    public boolean updateStatus(Long[] ids, String status, String typeName) {

        try {

            //修改商品的状态（上架/下架）
            goodsService.updataStatus(ids, status, typeName);

            //判断上架还是下架
            if ("1".equals(status)) {
                //创建商品索引，发送该消息
                jmsTemplate.send(solrQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                //创建商品详细页面，发送该消息（使用与上面不同的发送方式）
                for (Long id : ids) {
                    jmsTemplate.send(pageTopic, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id.toString());
                        }
                    });
                }

            } else {
                //删除商品索引，发送该消息
                jmsTemplate.send(solrDeleteQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                //删除商品详情静态页面,发送该消息（使用与上面不同的发送方式）
                for (Long id : ids) {
                    jmsTemplate.send(pageDeleteTopic, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id.toString());
                        }
                    });
                }


            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
