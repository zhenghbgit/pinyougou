package com.pinyougou.manager.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UploadController类
 * @Date: 2018/10/14 16:00
 * @Description:文件上传控制层
 * @Version：1.0
 */

@RestController
public class UploadController {

    @Value("${fileServerUrl}")
    private String fileServerUrl;

    //上传文件
    @RequestMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        //创建map集合,用户封装数据
        Map<String, Object> map = new HashMap<>();

        //默认返回状态
        map.put("status", 500);

        try {
            //加载配置文件
            String path = this.getClass().getResource("/fastdfs_client.conf").getPath();

            //初始化客户端全局对象
            ClientGlobal.init(path);
            //创建储存客户端对象
            StorageClient storageClient = new StorageClient();

            //获取原文件名
            String filename = multipartFile.getOriginalFilename();

            //上传文件
            String[] strings = storageClient.upload_file(multipartFile.getBytes(), FilenameUtils.getExtension(filename), null);

            //拼接图片地址访问路径
            StringBuilder sb = new StringBuilder(fileServerUrl);
            for (String string : strings) {
                sb.append("/" + string);
            }

            //修改返回状态
            map.put("status", 200);
            //返回拼接好的图片url地址
            map.put("url", sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        return map;
    }

}
