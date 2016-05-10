package com.example.jerryyin.ideacamera.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jerryyin.ideacamera.model.CameraModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 5/5/16.
 */
public class CameraModelService {

    private CameraDataBaseHelper dbHelper;

    public CameraModelService(Context context) {
        this.dbHelper = new CameraDataBaseHelper(context);
    }


    /**
     * 增
     *
     * @param model
     * @return
     */
    public boolean insertModel(CameraModel model) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into CameraModel (name, imgUris) values(?,?)";
        Object obj[] = {model.name, jointString(model.imgUris)};
        database.execSQL(sql, obj);
        return true;
    }

    /**
     * 删除 通过 model名字
     *
     * @param name
     * @return
     */
    public boolean deleteModelByName(String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        return true;
    }


    /**
     * 查询所有model
     * 不需要条件，直接查询所有的
     *
     * @return
     */
    public List<CameraModel> queryAllModel() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //查询CameraModel表中所有数据
        Cursor cursor = database.query("CameraModel", null, null, null, null, null, null);
        List<CameraModel> modelList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String Uris = cursor.getString(cursor.getColumnIndex("imgUris"));
                modelList.add(new CameraModel(name, splitUris(Uris)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelList;
    }


    /**
     * 查询 通过名字
     *
     * @param name
     * @return
     */
    public CameraModel queryModelByName(String name) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from CameraModel where name=? ";
        Cursor cursor = database.rawQuery(sql, new String[]{name + ""});
        if (cursor != null && cursor.moveToFirst()) {
            String Uris = cursor.getString(cursor.getColumnIndex("imgUris"));
            return new CameraModel(name, splitUris(Uris));
        }
        cursor.close();
        return null;
    }


    public boolean updateModel(CameraModel model) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "update CameraModel set imgUris=? where name=?;";
        Object obj[] = {
                jointString(model.imgUris),
                model.name};
        database.execSQL(sql, obj);
        return true;
    }


    /**
     * 拼接字符串 "|"
     * @param stringList
     * @return
     */
    public String jointString(List<String> stringList){
        String Uris = null;
        //拼接字符串
        for (String uri : stringList) {
            Uris += uri + "|";
        }
        return Uris;
    }

    /**
     * 工具方法
     * 分隔字符串 "|"
     *
     * @param Uris
     * @return
     */
    public List<String> splitUris(String Uris) {
        List<String> imgUris = new ArrayList<>();
        String[] arrayUri = Uris.split("\\|");  //分隔字符串
        for (int i = 0; i < arrayUri.length; i++) {
            imgUris.add(arrayUri[i]);
        }
        return imgUris;
    }
}
