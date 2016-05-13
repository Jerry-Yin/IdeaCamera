package com.example.jerryyin.ideacamera.util.common;


import com.example.jerryyin.ideacamera.base.ICApplication;

public class DistanceUtil {

    public static int getCameraAlbumWidth() {
        return (ICApplication.getApp().getScreenWidth() - ICApplication.getApp().dp2px(10)) / 4 - ICApplication.getApp().dp2px(4);
    }
    
    // 相机照片列表高度计算 
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + ICApplication.getApp().dp2px(4);
    }
    
    public static int getCameraPhotoWidth() {
        return ICApplication.getApp().getScreenWidth() / 4 - ICApplication.getApp().dp2px(2);
    }

    //活动标签页grid图片高度
    public static int getActivityHeight() {
        return (ICApplication.getApp().getScreenWidth() - ICApplication.getApp().dp2px(24)) / 3;
    }
}
