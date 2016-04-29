package com.example.jerryyin.ideacamera.util.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JerryYin on 4/20/16.
 */
public class ToastUtil {


    public static void showToast(Context context, String msg, int time){
        Toast.makeText(context, msg, time).show();
    }


}
