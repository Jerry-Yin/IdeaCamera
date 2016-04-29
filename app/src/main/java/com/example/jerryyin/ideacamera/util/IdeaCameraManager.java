package com.example.jerryyin.ideacamera.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.jerryyin.ideacamera.activity.CameraActivity;
import com.example.jerryyin.ideacamera.activity.ChooseActivity;
import com.example.jerryyin.ideacamera.model.PhotoItem;

import java.util.Stack;

/**
 * 相机管理类
 * Created by sky on 15/7/6.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class IdeaCameraManager {

    private static IdeaCameraManager mInstance;
    private Stack<Activity> cameras = new Stack<Activity>();

    public static IdeaCameraManager getInst() {
        if (mInstance == null) {
            synchronized (IdeaCameraManager.class) {
                if (mInstance == null)
                    mInstance = new IdeaCameraManager();
            }
        }
        return mInstance;
    }

    //打开照相界面
    public void openCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    //判断图片是否需要裁剪
    public void processPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
//        if (ImageUtils.isSquare(photo.getImageUri())) {
//            Intent newIntent = new Intent(activity, PhotoProcessActivity.class);
//            newIntent.setData(uri);
//            activity.startActivity(newIntent);
//        } else {
//            Intent i = new Intent(activity, CropPhotoActivity.class);
//            i.setData(uri);
//            //TODO稍后添加
//            activity.startActivityForResult(i, CameraAppConstants.REQUEST_CROP);
//        }

        Intent newIntent = new Intent(activity, ChooseActivity.class);
        newIntent.setData(uri);
        activity.startActivity(newIntent);
    }

    public void close() {
        for (Activity act : cameras) {
            try {
                act.finish();
            } catch (Exception e) {

            }
        }
        cameras.clear();
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }



}
