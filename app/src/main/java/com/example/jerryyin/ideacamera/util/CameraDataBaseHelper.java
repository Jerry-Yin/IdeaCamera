package com.example.jerryyin.ideacamera.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JerryYin on 5/5/16.
 */
public class CameraDataBaseHelper extends SQLiteOpenHelper {

    public static String mTableName = "IdeaCamera.db";
    public static int mDbVersion = 1;

    //varchar(n) 长度不固定且其最大长度为 n 的字串，n不能超过 4000。
    public static final String createModelTable = "create table CameraModel(" +
            "id integer primary key autoincrement, " +
            "name varchar not null, " +
            "imgUris varchar not null)";


    public CameraDataBaseHelper(Context context) {
        super(context, mTableName, null, mDbVersion);
    }

    public CameraDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(createModelTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
