package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.ItemSearchService;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：DeleteMessageListener类
 * @Date: 2018/10/25 0:23
 * @Description:删除消息监听者
 * @Version：1.0
 */

public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage> {

    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {

        //获取消息内容
        Long[] goodsIds = (Long[]) objectMessage.getObject();

        //删除索引库中的商品SKU
        itemSearchService.deleteItem(goodsIds);

    }
}
