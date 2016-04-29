package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.activity.ShowPhoActivity;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.common.DistanceUtil;
import com.example.jerryyin.ideacamera.util.common.ImageLoaderUtils;

import java.util.List;

/**
 * @author tongqian.ni
 */
public class GridViewAdapter extends BaseAdapter {

    private static final String TAG = "GridViewAdapter";
    private Context mContext;
    private List<PhotoItem> values;
    public static GalleryHolder holder;

    /**
     * @param context AlbumActivity
     * @param values
     */
    public GridViewAdapter(Context context, List<PhotoItem> values) {
        this.mContext = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GalleryHolder holder;
        int width = DistanceUtil.getCameraAlbumWidth();
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layout_item_gridview, null);
            holder = new GalleryHolder();
            holder.sample = (ImageView) convertView.findViewById(R.id.gallery_sample_image);
            holder.sample.setLayoutParams(new AbsListView.LayoutParams(width, width));
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }
        final PhotoItem gallery = (PhotoItem) getItem(position);

        holder.sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义界面查看
//                Intent intent =new Intent(mContext, ShowPhoActivity.class);
//                Uri uri = Uri.parse("file://"+gallery.getImageUri());
//                Log.d(TAG, "uri = "+uri);
//                intent.setData(uri);
//                mContext.startActivity(intent);

                //调用系统图库查看
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri mUri = Uri.parse("file://" + gallery.getImageUri());
                intent.setDataAndType(mUri, "image/*");
                mContext.startActivity(intent);
            }
        });

        ImageLoaderUtils.displayLocalImage(gallery.getImageUri(), holder.sample, null);

        return convertView;
    }

    class GalleryHolder {
        ImageView sample;

        public GalleryHolder() {

        }
    }


}
