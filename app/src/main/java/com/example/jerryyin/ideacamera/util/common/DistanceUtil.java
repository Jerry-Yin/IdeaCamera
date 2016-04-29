package com.example.jerryyin.ideacamera.util.common;


import com.example.jerryyin.ideacamera.base.CameraApplication;

public class DistanceUtil {

    public static int getCameraAlbumWidth() {
        return (CameraApplication.getApp().getScreenWidth() - CameraApplication.getApp().dp2px(10)) / 4 - CameraApplication.getApp().dp2px(4);
    }
    
    // 相机照片列表高度计算 
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + CameraApplication.getApp().dp2px(4);
    }
    
    public static int getCameraPhotoWidth() {
        return CameraApplication.getApp().getScreenWidth() / 4 - CameraApplication.getApp().dp2px(2);
    }

    //活动标签页grid图片高度
    public static int getActivityHeight() {
        return (CameraApplication.getApp().getScreenWidth() - CameraApplication.getApp().dp2px(24)) / 3;
    }
}
