package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;

import java.util.List;

/**
 * Created by JerryYin on 5/13/16.
 */
public class ModuleListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDataList;
    private LayoutInflater mInflater;


    public ModuleListAdapter(Context mContext, List<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(mContext);
    }

    class ViewHolder {
        ImageView img;
        TextView txt_name;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item_module_manager, null);
            holder.txt_name = (TextView) convertView.findViewById(R.id.tv_name_module);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        holder.txt_name.setText(mDataList.get(position));
        return convertView;
    }


}
