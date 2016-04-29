package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.common.ImageLoaderUtils;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;

import org.w3c.dom.Text;

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

    public CusGalleryAdapter(Context context, List<List<PhotoItem>> photoItems, List<String> stringList) {
        this.mContext = context;
        this.mListModelName = stringList;
        this.mPhotoItems = photoItems;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_item, null);
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
        if (mPhotoItems.get(position) == null || mPhotoItems.get(position).get(0)==null) {
            //默认图标
            Bitmap bitmap = ImageUtils.getImageBitmap(mContext.getResources(),
                    mDefaultIconID);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            drawable.setAntiAlias(true); // 消除锯齿
            holder.img.setImageDrawable(drawable);
        } else {
            //第一张图片
            //第三方图片加载工具 ,知恩感通过地址加载，就没有3D效果
            ImageLoaderUtils.displayLocalImage(mPhotoItems.get(position).get(0).getImageUri(), holder.img, null);


            //自己根据地址加载
//            String imgUri = mPhotoItems.get(position).get(0).getImageUri();
//            Bitmap bitmap = ImageUtils.decodeBitmapFromPath(imgUri);
//            Log.d(TAG, "imgUri = "+imgUri);
//            Log.d(TAG, "bitmap = "+bitmap);
//            Bitmap okBitmap = ImageUtils.getImageBitmap(bitmap, position);
//            holder.img.setImageBitmap(okBitmap);
        }

//        Bitmap bitmap = Bi

        Gallery.LayoutParams params = new Gallery.LayoutParams(480, 640);   //画廊效果的单个ITEM大小
        convertView.setLayoutParams(params);
        return convertView;

    }
}
