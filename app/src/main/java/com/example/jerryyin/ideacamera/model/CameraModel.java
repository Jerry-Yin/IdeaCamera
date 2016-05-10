package com.example.jerryyin.ideacamera.model;

import java.util.List;

/**
 * Created by JerryYin on 5/5/16.
 * 用户添加的模版
 */
public class CameraModel {

    public String name;
    public String imgUri;
    //一个model下有很多张照片； 存储时候把路径拼接成一个字符串存储进数据库
    public List<String> imgUris;


    public CameraModel(String name, List<String> imgUris) {
        this.name = name;
        this.imgUris = imgUris;
    }
}
