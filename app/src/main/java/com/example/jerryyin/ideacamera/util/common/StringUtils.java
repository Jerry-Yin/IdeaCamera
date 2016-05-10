package com.example.jerryyin.ideacamera.util.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 5/10/16.
 */
public class StringUtils {

    /**
     * 拼接字符串 "|"
     * @param stringList
     * @return
     */
    public static String jointString(List<String> stringList){
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
    public static List<String> splitUris(String Uris) {
        List<String> imgUris = new ArrayList<>();
        String[] arrayUri = Uris.split("\\|");  //分隔字符串
        for (int i = 0; i < arrayUri.length; i++) {
            imgUris.add(arrayUri[i]);
        }
        return imgUris;
    }
}
