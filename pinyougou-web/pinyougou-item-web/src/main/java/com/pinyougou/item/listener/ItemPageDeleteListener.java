package com.pinyougou.item.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.File;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemPageDeleteListener类
 * @Date: 2018/10/25 19:14
 * @Description:商品详细页面删除监听器
 * @Version：1.0
 */

public class ItemPageDeleteListener implements SessionAwareMessageListener<TextMessage> {

    //注入属性配置
    @Value("${page.dir}")
    private String pageDir;

    @Override
    public void onMessage(TextMessage textMessage, Session session) throws JMSException {

        //获取消息内容
        String goodId = textMessage.getText();

        File file = new File(pageDir + goodId + ".html");
        //判断文件是否存在
        if (file.exists()) {
            //如果存在，就删除
            file.delete();
        }

    }
}
