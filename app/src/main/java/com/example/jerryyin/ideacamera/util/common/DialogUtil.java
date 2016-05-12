package com.example.jerryyin.ideacamera.util.common;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by JerryYin on 4/20/16.
 */
public class DialogUtil {

    static ProgressDialog mProgressDialog = null;
    DialogUtil mDialogUtil;

//    private DialogUtil(Context context) {
//        this.mProgressDialog = new ProgressDialog(context);
//    }

//    public static DialogUtil getInstance(Context context){
//        if (mProgressDialog == null){
//            mProgressDialog = new ProgressDialog(context);
//        }
//        return new DialogUtil();
//    }


    public static void showProgressDialog(Context context, String message){
        mProgressDialog = new ProgressDialog(context);
//        if (mProgressDialog != null){
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
//        }
    }

    public static void dismissDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }



}
