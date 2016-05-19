package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 5/18/16.
 */
public class NormalGalleryAdapter extends BaseAdapter {

    private static final String TAG = "NormalGalleryAdapter";

    private List<Bitmap> mBitmapList;
    private List<String> mNameList;
    private List<Map<String, Bitmap>> mMapList;
    private Context mContext;

//    public NormalGalleryAdapter(List<Map<String, Bitmap>> mBitmapList, Context mContext) {
//        this.mBitmapList = mBitmapList;
//        this.mContext = mContext;
//    }

    public NormalGalleryAdapter(List<Map<String, Bitmap>> mMapList, Context mContext) {
        this.mMapList = mMapList;
        this.mContext = mContext;
//        setListData(mMapList);
    }

//    public void setListData(List<Map<String,Bitmap>> map) {
//        map.
//
//    }

    class ViewHolder{
        ImageView img;
        TextView txt;
    }

    @Override
    public int getCount() {
        return mMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
//            View view = new EditText(mContext);
//            convertView = view;
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_gallery_effect, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_photo);
            holder.txt = (TextView) convertView.findViewById(R.id.txt_effect);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);

        String name = null;
        Bitmap bitmap = null;
        Map map = mMapList.get(position);
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry entry = (Map.Entry) i.next();
            name = (String) entry.getKey();
            bitmap = (Bitmap) entry.getValue();
        }
        holder.img.setImageBitmap(bitmap);
        holder.img.setLayoutParams(new LinearLayout.LayoutParams(180, 180));
//        Log.d(TAG, "width = "+bitmap.getWidth()/5+ "height = "+bitmap.getHeight()/5);
        holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.txt.setText(name);
//        Gallery.LayoutParams params = new Gallery.LayoutParams(300, 160);
//        convertView.setLayoutParams(params);
        return convertView;
    }
}
