package com.example.jerryyin.ideacamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;
import com.example.jerryyin.ideacamera.view.CusDeleteItemLayout;

import java.util.List;

/**
 * Created by JerryYin on 5/13/16.
 */
public class ModuleListAdapter extends BaseAdapter  {

    private Context mContext;
    private List<String> mDataList;
    private LayoutInflater mInflater;
    public static CusDeleteItemLayout itemDelete = null;


    public ModuleListAdapter(Context mContext, List<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(mContext);
    }

    class ViewHolder {
        ImageView img;
        TextView txt_name;
        Button btn_to_top, btn_delete;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item_module_manager, null);
            holder.txt_name = (TextView) convertView.findViewById(R.id.tv_name_module);
            holder.btn_to_top = (Button) convertView.findViewById(R.id.btn_to_top);
            holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_name.setText(mDataList.get(position));

        holder.btn_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mContext, "置顶", Toast.LENGTH_SHORT);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mContext, "删除", Toast.LENGTH_SHORT);
                mDataList.remove(position);
                notifyDataSetChanged();
                ItemDeleteReset();

            }
        });

        return convertView;
    }


    //重置ListView item 位置
    public static void ItemDeleteReset() {
        if (itemDelete != null) {
            itemDelete.reSet();
        }
    }

}
