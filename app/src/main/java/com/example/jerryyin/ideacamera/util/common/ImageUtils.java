package com.example.jerryyin.ideacamera.util.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.jerryyin.ideacamera.base.ICApplication;
import com.example.jerryyin.ideacamera.model.Album;
import com.example.jerryyin.ideacamera.model.PhotoItem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * 图片工具类
 */
public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /** 缓存集合 */
    private static Hashtable<Integer, SoftReference<Bitmap>> mImageCache = new Hashtable<Integer, SoftReference<Bitmap>>();
    private static Hashtable<Integer, SoftReference<Bitmap>> mImageCache2 = new Hashtable();


    public static int getMiniSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return Math.min(options.outHeight, options.outWidth);
    }

    public static boolean isSquare(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return options.outHeight == options.outWidth;
    }

    //图片是不是正方形
    public static boolean isSquare(Uri imageUri) {
        ContentResolver resolver = ICApplication.getApp().getContentResolver();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(imageUri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return options.outHeight == options.outWidth;
    }

    //保存图片文件
    public static String saveToFile(String fileFolderStr, boolean isDir, Bitmap croppedImage) throws FileNotFoundException, IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);  //系统相册路径
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtils.getInst().mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtils.getInst().mkdir(jpgFile.getParentFile());
            }
        }
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流

        croppedImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        IOUtil.closeStream(outputStream);
        return jpgFile.getPath();
    }



    //从文件中读取Bitmap
    public static Bitmap decodeBitmapWithOrientation(String pathName, int width, int height) {
        return decodeBitmapWithSize(pathName, width, height, false);
    }

    public static Bitmap decodeBitmapWithOrientationMax(String pathName, int width, int height) {
        return decodeBitmapWithSize(pathName, width, height, true);
    }

    private static Bitmap decodeBitmapWithSize(String pathName, int width, int height,
                                               boolean useBigger) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inInputShareable = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(pathName, options);

        int decodeWidth = width, decodeHeight = height;
        final int degrees = getImageDegrees(pathName);
        if (degrees == 90 || degrees == 270) {
            decodeWidth = height;
            decodeHeight = width;
        }

        if (useBigger) {
            options.inSampleSize = (int) Math.min(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        } else {
            options.inSampleSize = (int) Math.max(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        }

        options.inJustDecodeBounds = false;
        Bitmap sourceBm = BitmapFactory.decodeFile(pathName, options);
        return imageWithFixedRotation(sourceBm, degrees);
    }

    /**
     * 从path中获取图片信息
     * @param path
     * @return
     */
    public static final float DISPLAY_WIDTH = 200;
    public static final float DISPLAY_HEIGHT = 200;
    public static Bitmap decodeBitmapFromPath(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int)Math.ceil(op.outWidth/DISPLAY_WIDTH);
        int hRatio = (int)Math.ceil(op.outHeight/DISPLAY_HEIGHT);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }




    public static int getImageDegrees(String pathName) {
        int degrees = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(pathName);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degrees;
    }

    public static Bitmap imageWithFixedRotation(Bitmap bm, int degrees) {
        if (bm == null || bm.isRecycled())
            return null;

        if (degrees == 0)
            return bm;

        final Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (result != bm)
            bm.recycle();
        return result;

    }


    public static float getImageRadio(ContentResolver resolver, Uri fileUri) {
        InputStream inputStream = null;
        try {
            inputStream = resolver.openInputStream(fileUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            int initWidth = options.outWidth;
            int initHeight = options.outHeight;
            float rate = initHeight > initWidth ? (float) initHeight / (float) initWidth
                    : (float) initWidth / (float) initHeight;
            return rate;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            IOUtil.closeStream(inputStream);
        }

    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Map<String, Album> findGalleries(Context mContext, List<String> paths, long babyId) {
        paths.clear();
        paths.add(FileUtils.getInst().getSystemPhotoPath());
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED };//FIXME 拍照时间为新增照片时间
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,//指定所要查询的字段
                MediaStore.Images.Media.SIZE + ">?",//查询条件
                new String[] { "100000" }, //查询条件中问号对应的值
                MediaStore.Images.Media.DATE_ADDED + " desc");

        cursor.moveToFirst();
        //文件夹照片
        Map<String, Album> galleries = new HashMap<String, Album>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(1);
            if (data.lastIndexOf("/") < 1) {
                continue;
            }
            String sub = data.substring(0, data.lastIndexOf("/"));
            if (!galleries.keySet().contains(sub)) {
                String name = sub.substring(sub.lastIndexOf("/") + 1, sub.length());
                if (!paths.contains(sub)) {
                    paths.add(sub);
                }
                galleries.put(sub, new Album(name, sub, new ArrayList<PhotoItem>()));
            }

            galleries.get(sub).getPhotos().add(new PhotoItem(data, (long) (cursor.getInt(2)) * 1000));
        }
        //系统相机照片
        ArrayList<PhotoItem> sysPhotos = FileUtils.getInst().findPicsInDir(
                FileUtils.getInst().getSystemPhotoPath());
        if (!sysPhotos.isEmpty()) {
            galleries.put(FileUtils.getInst().getSystemPhotoPath(), new Album("系统相册", FileUtils
                    .getInst().getSystemPhotoPath(), sysPhotos));
        } else {
            galleries.remove(FileUtils.getInst().getSystemPhotoPath());
            paths.remove(FileUtils.getInst().getSystemPhotoPath());
        }
        return galleries;
    }



    //异步加载图片
    public static interface LoadImageCallback {
        public void callback(Bitmap result);
    }

    public static void asyncLoadImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadImageUriTask(context, imageUri, callback).execute();
    }

    private static class LoadImageUriTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri         imageUri;
        private final Context     context;
        private LoadImageCallback callback;

        public LoadImageUriTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream inputStream;
                if (imageUri.getScheme().startsWith("http")
                        || imageUri.getScheme().startsWith("https")) {
                    inputStream = new URL(imageUri.toString()).openStream();
                } else {
                    inputStream = context.getContentResolver().openInputStream(imageUri);
                }
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }
    }

    //异步加载缩略图
    public static void asyncLoadSmallImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadSmallPicTask(context, imageUri, callback).execute();
    }

    private static class LoadSmallPicTask extends AsyncTask<Void, Void, Bitmap> {

        private final Uri         imageUri;
        private final Context     context;
        private LoadImageCallback callback;

        public LoadSmallPicTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getResizedBitmap(context, imageUri, 300, 300);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }

    }

    //得到指定大小的Bitmap对象
    public static Bitmap getResizedBitmap(Context context, Uri imageUri, int width, int height) {
        InputStream inputStream = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            inputStream = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(inputStream, null, options);

            options.outWidth = width;
            options.outHeight = height;
            options.inJustDecodeBounds = false;
            IOUtil.closeStream(inputStream);
            inputStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeStream(inputStream);
        }
        return null;
    }


    /***************************
     *
     *  3D画廊部分
     */

    /**
     * 根据id返回一个处理后的图片
     * @param res
     * @param resID
     * @return
     */
    public static Bitmap getImageBitmap(Resources res, int resID) {
        // 先去集合中取当前resID是否已经拿过图片，如果集合中有，说明已经拿过，直接使用集合中的图片返回
        SoftReference<Bitmap> reference = mImageCache.get(resID);
        if (reference != null) {
            Bitmap bitmap = reference.get();
            if (bitmap != null) {// 从内存中取
                Log.i(TAG, "从内存中取");
                return bitmap;
            }
        }
        // 如果集合中没有，就调用getInvertImage得到一个图片，需要向集合中保留一张，最后返回当前图片
        Log.i(TAG, "重新加载");
        Bitmap invertBitmap = getInvertBitmap(res, resID);
        // 在集合中保存一份，便于下次获取时直接在集合中获取
        mImageCache.put(resID, new SoftReference<Bitmap>(invertBitmap));
        return invertBitmap;
    }

    /**
     * 根据图片的id，获取到处理之后的图片
     *
     * @param resID
     * @return
     */
    public static Bitmap getInvertBitmap(Resources res, int resID) {
        // 1.获取原图
        Bitmap sourceBitmap = BitmapFactory.decodeResource(res, resID);

        // 2.生成倒影图片
        Matrix m = new Matrix(); // 图片矩阵
        m.setScale(1.0f, -1.0f); // 让图片按照矩阵进行反转
        Bitmap invertBitmap = Bitmap.createBitmap(sourceBitmap, 0,
                sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(),
                sourceBitmap.getHeight() / 2, m, false);

        // 3.两张图片合成一张图片
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
                (int) (sourceBitmap.getHeight() * 1.5 + 5), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap); // 为合成图片指定一个画板
        canvas.drawBitmap(sourceBitmap, 0f, 0f, null); // 将原图片画在画布的上方
        canvas.drawBitmap(invertBitmap, 0f, sourceBitmap.getHeight() + 5, null); // 将倒影图片画在画布的下方

        // 4.添加遮罩效果
        Paint paint = new Paint();
        // 设置遮罩的颜色，这里使用的是线性梯度
        LinearGradient shader = new LinearGradient(0,
                sourceBitmap.getHeight() + 5, 0, resultBitmap.getHeight(),
                0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // 设置模式为：遮罩，取交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, sourceBitmap.getHeight() + 5,
                sourceBitmap.getWidth(), resultBitmap.getHeight(), paint);

        return resultBitmap;
    }



    /********
     *
     */

    public static Bitmap getImageBitmap(Bitmap bmp, int key){
        // 先去集合中取当前resID是否已经拿过图片，如果集合中有，说明已经拿过，直接使用集合中的图片返回
        SoftReference<Bitmap> reference = mImageCache2.get(key);
        if (reference != null) {
            Bitmap bitmap = reference.get();
            if (bitmap != null) {// 从内存中取
                Log.i(TAG, "从内存中取");
                return bitmap;
            }
        }
        // 如果集合中没有，就调用getInvertImage得到一个图片，需要向集合中保留一张，最后返回当前图片
        Log.i(TAG, "重新加载");
        Bitmap invertBitmap = getInvertBitmap(bmp);
        // 在集合中保存一份，便于下次获取时直接在集合中获取
        mImageCache.put(key, new SoftReference<Bitmap>(invertBitmap));
        return invertBitmap;
    }

    public static Bitmap getInvertBitmap(Bitmap sourceBitmap){
        // 2.生成倒影图片
        Matrix m = new Matrix(); // 图片矩阵
        m.setScale(1.0f, -1.0f); // 让图片按照矩阵进行反转
        Bitmap invertBitmap = Bitmap.createBitmap(sourceBitmap, 0,
                sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(),
                sourceBitmap.getHeight() / 2, m, false);

        // 3.两张图片合成一张图片
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
                (int) (sourceBitmap.getHeight() * 1.5 + 5), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap); // 为合成图片指定一个画板
        canvas.drawBitmap(sourceBitmap, 0f, 0f, null); // 将原图片画在画布的上方
        canvas.drawBitmap(invertBitmap, 0f, sourceBitmap.getHeight() + 5, null); // 将倒影图片画在画布的下方

        // 4.添加遮罩效果
        Paint paint = new Paint();
        // 设置遮罩的颜色，这里使用的是线性梯度
        LinearGradient shader = new LinearGradient(0,
                sourceBitmap.getHeight() + 5, 0, resultBitmap.getHeight(),
                0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // 设置模式为：遮罩，取交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, sourceBitmap.getHeight() + 5,
                sourceBitmap.getWidth(), resultBitmap.getHeight(), paint);

        return resultBitmap;
    }

}
