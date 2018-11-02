package com.pinyougou.shop.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：UploadController类
 * @Date: 2018/10/10 19:05
 * @Description:上传控制器
 * @Version：1.0
 */

@RestController
public class UploadController {

    //注入文件服务器访问地址
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        //创建map集合，用于封装数据
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("status", 500);

        try {
            //加载配置文件
            String path = this.getClass().getResource("/fastdfs_client.conf").getPath();
            //初始化客户端全局对象
            ClientGlobal.init(path);
            //创建存储客户端对象
            StorageClient storageClient = new StorageClient();
            //获取原文件名字
            String originalFilename = multipartFile.getOriginalFilename();

            //上传文件
            String[] strings = storageClient.upload_file(multipartFile.getBytes(), FilenameUtils.getExtension(originalFilename), null);

            //拼接地址图片访问路径
            StringBuilder urlSB = new StringBuilder(fileServerUrl);
            for (String string : strings) {
                urlSB.append("/" + string);
            }

            dataMap.put("status", 200);
            dataMap.put("url", urlSB.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataMap;
    }

}
