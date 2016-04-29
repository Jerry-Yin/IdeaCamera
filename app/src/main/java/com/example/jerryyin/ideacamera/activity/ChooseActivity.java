package com.example.jerryyin.ideacamera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 4/21/16.
 */
public class ChooseActivity extends BaseActivity {

    @Bind(R.id.img_photo)
    ImageView mImgPhoto;
    @Bind(R.id.btn_add_model)
    FloatingActionButton mBtnAddModel;
    @Bind(R.id.btn_cancle)
    Button mBtnCancle;
    @Bind(R.id.btn_save)
    Button mBtnSave;

    //当前图片
    private Bitmap mCurrentBitmap;
    private Uri mCurBmpUri; //当前照片的uri
    private String mCurModel;   //将要设定的模版
    private boolean isHaveModel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //应用程序的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //系统的状态栏
        setContentView(R.layout.layout_choose);
        ButterKnife.bind(this);

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                mCurrentBitmap = result;
                mCurBmpUri = getIntent().getData();
                mImgPhoto.setImageBitmap(mCurrentBitmap);
            }
        });

    }


    @OnClick({R.id.btn_add_model, R.id.btn_cancle, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_model:
                // TODO: 4/22/16 有模版就选择或者添加； 没有就添加

                View inflate = getLayoutInflater().inflate(R.layout.layout_add_model, null);
                final EditText etModel = (EditText) inflate.findViewById(R.id.et_model);
                final ListView listModel = (ListView) inflate.findViewById(R.id.list_model);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (!TextUtils.isEmpty(mCurModel)){
                    etModel.setText(mCurModel);
                    etModel.setSelection(mCurModel.length());
                }
                if (isHaveModel){
                    listModel.setVisibility(View.VISIBLE);
                }else {
                    listModel.setVisibility(View.GONE);
                }
                listModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listModel.getItemAtPosition(position);
//                        mCurModel = ;
                        ToastUtil.showToast(ChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                    }
                });
                builder.setTitle("添加模版")
                        .setView(inflate)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(etModel.getText())) {
                                    mCurModel = etModel.getText().toString();
                                    ToastUtil.showToast(ChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                                } else {
                                    return;
                                }
//                                ToastUtil.showToast(ChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.create().dismiss();
                            }
                        })
                        .create().show();

                break;

            case R.id.btn_cancle:
                this.finish();
                break;

            case R.id.btn_save:
                Intent intent = new Intent(ChooseActivity.this, GalleryActivity.class);
                intent.putExtra("model", mCurModel);
//                intent.putExtra("imgUri", mCurBmpUri);
                intent.setData(mCurBmpUri);
                startActivity(intent);
//                ChooseActivity.this.finish();
                break;
        }
    }
}
