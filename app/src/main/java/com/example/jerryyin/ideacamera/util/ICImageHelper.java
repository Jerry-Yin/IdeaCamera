package com.example.jerryyin.ideacamera.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by JerryYin on 12/7/15.
 * 图片管理工具类
 * 可以调整图片的 色相，饱和度， 亮度
 */
public class ICImageHelper {
    /**
     * @param bitmap     待处理图片
     * @param hue        色相（RGB三原色 0-红 1－绿 2-蓝）
     * @param saturation 饱和度（0-2）
     * @param light      亮度
     * @return 处理后的图片
     * <p>
     * 注意，不能直接对从文件流接收过来的bitmap进行处理，需要新建一个临时变量加以处理
     */
    public static Bitmap handleImageEffect(Bitmap bitmap, float hue, float saturation, float light) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);   //临时变量
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //色相
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);

        //饱和度
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        //亮度
        ColorMatrix lightMatrix = new ColorMatrix();
        lightMatrix.setScale(light, light, light, 1);     //参数：R， G， B，A（透明度）;

        //综合三者
        ColorMatrix imgMatrix = new ColorMatrix();
        imgMatrix.postConcat(hueMatrix);
        imgMatrix.postConcat(saturationMatrix);
        imgMatrix.postConcat(lightMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imgMatrix));     //设置完成后的画笔

        /**
         * 注意此处 不是重画 bmp, 而是 bitmap
         */
        canvas.drawBitmap(bitmap, 0, 0, paint);    //用设置完的画笔重新绘制,

        return bmp;
    }

    /**
     * 底片效果
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleBottomImgEffect(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int heigh = bitmap.getHeight();

        int[] oldPix = new int[width * heigh]; //原图的像素点
        int[] newPix = new int[width * heigh]; //底片像素点

        Bitmap bmp = Bitmap.createBitmap(width, heigh, Bitmap.Config.ARGB_8888);

        bitmap.getPixels(oldPix, 0, width, 0, 0, width, heigh);    //获取原图的像素点

        for (int i = 0; i < width * heigh; i++) {
            int r, g, b, a;
            r = Color.red(oldPix[i]);
            g = Color.green(oldPix[i]);
            b = Color.blue(oldPix[i]);
            a = Color.alpha(oldPix[i]);

            //底片效果算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            //保证 三原色不超出范围 0-－255 此部分必须要，否则效果会不对
            r = makeSureEffect(r);
            g = makeSureEffect(g);
            b = makeSureEffect(b);

            newPix[i] = Color.argb(a, r, g, b); //新的效果像素点
        }

        bmp.setPixels(newPix, 0, width, 0, 0, width, heigh);

        return bmp;
    }


    /**
     * 怀旧，老照片效果
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleOldImgEffect(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int oldPix[] = new int[width * height];
        int newPix[] = new int[width * height];

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmap.getPixels(oldPix, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            int a, r, g, b;
            r = Color.red(oldPix[i]);
            g = Color.green(oldPix[i]);
            b = Color.blue(oldPix[i]);
            a = Color.alpha(oldPix[i]);

            //老照片算法
            r = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            //保证 三原色不超出范围 0-－255
            r = makeSureEffect(r);
            g = makeSureEffect(g);
            b = makeSureEffect(b);

            newPix[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPix, 0, width, 0, 0, width, height);

        return bmp;
    }

    /**
     * 浮雕效果,需要用到前一个像素点的像素值（循环时 i 从 1 开始）
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleReliefImgEffect(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int oldPix[] = new int[width * height];
        int newPix[] = new int[width * height];

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmap.getPixels(oldPix, 0, width, 0, 0, width, height);

        for (int i = 1; i < width * height; i++) {
            int a, r, g, b;
            r = Color.red(oldPix[i]);
            g = Color.green(oldPix[i]);
            b = Color.blue(oldPix[i]);
            a = Color.alpha(oldPix[i]);

            //前一个点的像素值
            int a1 = Color.alpha(oldPix[i - 1]);
            int r1 = Color.red(oldPix[i - 1]);
            int g1 = Color.green(oldPix[i - 1]);
            int b1 = Color.blue(oldPix[i - 1]);

            //老照片算法
            r = r1 - r + 127;
            g = g1 - g + 127;
            b = b1 - b + 127;

            //保证 三原色不超出范围 0-－255
            r = makeSureEffect(r);
            g = makeSureEffect(g);
            b = makeSureEffect(b);


            newPix[i] = Color.argb(a1, r, g, b);
        }
        bmp.setPixels(newPix, 0, width, 0, 0, width, height);

        return bmp;
    }


    //保证 三原色不超出范围 0-－255 此部分必须要，否则效果会不对
    private static int makeSureEffect(int r) {
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }
        return r;
    }

}
