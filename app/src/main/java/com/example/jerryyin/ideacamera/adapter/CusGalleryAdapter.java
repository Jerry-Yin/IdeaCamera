package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.CameraAppConstants;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.common.ImageLoaderUtils;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;

import java.util.List;


/**
 * Created by JerryYin on 1/25/16.
 */
public class CusGalleryAdapter extends BaseAdapter {

    private static final String TAG = "CusGalleryAdapter";
    public int mDefaultIconID = R.drawable.ic_wallpaper_white_24dp; //默认图标
    private Context mContext;
    private List<String> mListModelName;
    private List<List<PhotoItem>> mPhotoItems;
    private String mCurReflect;     //效果

    public CusGalleryAdapter(Context context, List<List<PhotoItem>> photoItems, List<String> stringList) {
        this.mContext = context;
        this.mListModelName = stringList;
        this.mPhotoItems = photoItems;
        this.mCurReflect = mContext.getSharedPreferences(CameraAppConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(CameraAppConstants.KEY_REFLECT, CameraAppConstants.ITEM_REFLECTS[0]);
    }

    class GalleryHolder {
        ImageView img;
        TextView txt;
    }

    @Override
    public int getCount() {
        return mPhotoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_gallery, null);
            holder = new GalleryHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.image_item);
            holder.txt = (TextView) convertView.findViewById(R.id.tv_model_name);
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }


        if (mListModelName.size() != 0 && mListModelName.get(position) != null) {
            holder.txt.setText(mListModelName.get(position));
        }

        //设置gallery图标
        if (mPhotoItems.size() < position + 1) {
            holder.img.setImageDrawable(setNormalIcon());
        } else if (mPhotoItems.get(position) == null || mPhotoItems.get(position).get(0) == null) {
            holder.img.setImageDrawable(setNormalIcon());
        } else {
            final String imgUri = mPhotoItems.get(position).get(0).getImageUri();

            if (!TextUtils.isEmpty(mCurReflect)) {
                if (mCurReflect.equals(CameraAppConstants.ITEM_REFLECTS[0])) {
                    //第三方图片加载工具 ,知恩感通过地址加载，就没有3D效果
                    ImageLoaderUtils.displayLocalImage(mPhotoItems.get(position).get(0).getImageUri(), holder.img, null);

                } else if (mCurReflect.equals(CameraAppConstants.ITEM_REFLECTS[1])) {
                    Bitmap bitmap = ImageUtils.decodeBitmapFromPath(imgUri);
                    Log.d(TAG, "imgUri = " + imgUri);
                    Log.d(TAG, "bitmap = " + bitmap);     //null
                    Bitmap okBitmap = ImageUtils.getImageBitmap(bitmap, position);
//            ImageLoaderUtils.displayLocalImage(okBitmap.get,);
                    holder.img.setImageBitmap(okBitmap);
                }
            } else {
                holder.img.setImageDrawable(setNormalIcon());
            }


            //第一张图片

            //自己根据地址加载(gallery1)
//            final String imgUri = mPhotoItems.get(position).get(0).getImageUri();
//            final Bitmap[] bitmap = {null};
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    bitmap[0] = ImageUtils.decodeBitmapFromPath(imgUri);
//                }
//            }).start();
//            Bitmap bitmap = ImageUtils.decodeBitmapFromPath(imgUri);
//            Log.d(TAG, "imgUri = " + imgUri);
//            Log.d(TAG, "bitmap = " + bitmap);     //null
//            Bitmap okBitmap = ImageUtils.getImageBitmap(bitmap, position);
////            ImageLoaderUtils.displayLocalImage(okBitmap.get,);
//            holder.img.setImageBitmap(okBitmap);

            //自己根据地址加载（gallery2）
//            String imgUri = mPhotoItems.get(position).get(0).getImageUri();
//            Log.d(TAG, "imgUri = " + imgUri);
//            Bitmap b = ImageUtils.decodeBitmapFromPath(imgUri);
//            Bitmap bitmap = BitmapUtil.createReflectedBitmap(b);
////            Bitmap okBitmap = ImageUtils.getImageBitmap(bitmap, position);
//            holder.img.setImageBitmap(bitmap);
        }

//        Bitmap bitmap = Bi

        //获取设备屏幕尺寸
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        Log.d(TAG, "屏幕尺寸：width = " + width + " height = " + height);
        Gallery.LayoutParams params = null;
        if (width > 500) {
            params = new Gallery.LayoutParams(480, 640);   //画廊效果的单个ITEM大小
//            params = new Gallery.LayoutParams(520, 1092);   //画廊效果的单个ITEM大小
        } else {
            params = new Gallery.LayoutParams(240, 320);   //画廊效果的单个ITEM大小
        }
        convertView.setLayoutParams(params);
        return convertView;

    }


    //设置默认图标
    public Drawable setNormalIcon() {
        Bitmap bitmap = ImageUtils.getImageBitmap(mContext.getResources(),
                mDefaultIconID);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setAntiAlias(true); // 消除锯齿
        return drawable;
    }
}
