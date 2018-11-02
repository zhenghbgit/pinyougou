package com.pinyougou.item.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.GoodsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemPageListener类
 * @Date: 2018/10/25 16:29
 * @Description:创建商品详细页面监听器
 * @Version：1.0
 */

public class ItemPageListener implements SessionAwareMessageListener<TextMessage> {

    //注入模板配置对象
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    //引入商品服务
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    //注入属性配置
    @Value("${page.dir}")
    private String pageDir;

    @Override
    public void onMessage(TextMessage textMessage, Session session) throws JMSException {

        try {

            //获取消息内容
            String goodId = textMessage.getText();

            //获取模板文件对象
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("item.ftl");

            //获取模板数据
            Map<String, Object> dataModel = goodsService.findGoodsByGoodsId(Long.valueOf(goodId));

            //创建转换流对象（根据字节输出流转化为字符输出流）
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(pageDir + goodId + ".html"), "UTF-8");

            //填充模板
            template.process(dataModel, outputStreamWriter);

            //关闭输出流
            outputStreamWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
