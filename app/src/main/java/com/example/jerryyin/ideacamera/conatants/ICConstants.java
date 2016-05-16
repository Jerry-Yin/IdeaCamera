package com.example.jerryyin.ideacamera.conatants;

import android.os.Environment;

/**
 * Created by sky on 2015/7/6.
 */
public class ICConstants {


    public static final String APP_DIR = Environment.getExternalStorageDirectory() + "/IdeaCamera";
    public static final String APP_TEMP = APP_DIR + "/temp";
    public static final String APP_IMAGE = APP_DIR + "/image";

    public static final int POST_TYPE_POI = 1;
    public static final int POST_TYPE_TAG = 0;
    public static final int POST_TYPE_DEFAULT = 0;


    public static final float DEFAULT_PIXEL = 1242;                           //按iphone6设置
    public static final String PARAM_MAX_SIZE = "PARAM_MAX_SIZE";
    public static final String PARAM_EDIT_TEXT = "PARAM_EDIT_TEXT";
    public static final int ACTION_EDIT_LABEL = 8080;
    public static final int ACTION_EDIT_LABEL_POI = 9090;

    public static final String FEED_INFO = "FEED_INFO";


    public static final int REQUEST_CROP = 6709;
    public static final int REQUEST_PICK = 9162;
    public static final int RESULT_ERROR = 404;

    /**
     * 用户设置
     */

    //用户选择的画廊动画效果(默认第1种)
    public static final String KEY_REFLECT = "reflect_gallery";
    public static final String KEY_REFLECT_CUR = "reflect_gallery_cur";
    public static final String[] ITEM_REFLECTS = new String[]{"普通3D", "炫酷3D"};
    public final static String PREFERENCE_NAME = "ideaCamera_Pref";  //pref 表名称

    //用户照片存储路径 数据设置
    public static final String KEY_IMG_DIR = "image_directory";


}
