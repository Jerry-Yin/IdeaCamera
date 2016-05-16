package com.example.jerryyin.ideacamera.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.util.common.DialogUtil;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;
import com.example.jerryyin.ideacamera.view.CusDeleteItemLayout;

import java.util.List;

/**
 * Created by JerryYin on 5/13/16.
 */
public class ModuleListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDataList;
    private LayoutInflater mInflater;
    public static CusDeleteItemLayout itemDelete = null;
    private CameraModelService mModelService;


    public ModuleListAdapter(Context mContext, List<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(mContext);
        mModelService = new CameraModelService(mContext);
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
                final EditText txt = new EditText(mContext);
                String newName;
                txt.setText(mDataList.get(position));
                txt.setSelection(mDataList.get(position).length());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("修改模版名称")
                        .setView(txt)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(txt.getText().toString())) {
                                    Toast.makeText(mContext, "名字不能为空!", Toast.LENGTH_SHORT);
                                }
                                if (txt.getText().toString().equals(mDataList.get(position))){
                                    Toast.makeText(mContext, "请输入与之前不一样的名字!", Toast.LENGTH_SHORT);
                                }
                                else {
                                    boolean isRenamed = mModelService.reNameModel(mDataList.get(position), txt.getText().toString());
                                    if (isRenamed) {
                                        ToastUtil.showToast(mContext, "修改成功！", Toast.LENGTH_SHORT);
//                                        dialog.dismiss();
                                        mDataList.set(position, txt.getText().toString());
                                        notifyDataSetChanged();
                                    } else {
                                        ToastUtil.showToast(mContext, "修改失败！", Toast.LENGTH_SHORT);
//                                        dialog.dismiss();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/13/16 删除module (注意需要先删除数据库中的数据，再移除list 中的数据)
                boolean isDeleted = mModelService.deleteModelByName(mDataList.get(position));
                if (isDeleted) {
                    ToastUtil.showToast(mContext, "删除完毕", Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.showToast(mContext, "删除失败", Toast.LENGTH_SHORT);
                }
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
