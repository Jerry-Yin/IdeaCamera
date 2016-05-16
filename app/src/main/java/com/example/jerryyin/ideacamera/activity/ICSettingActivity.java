package com.example.jerryyin.ideacamera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.conatants.ICConstants;
import com.example.jerryyin.ideacamera.util.common.FileUtils;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 5/12/16.
 */
public class ICSettingActivity extends ICBaseActivity {

    private static final String TAG = "ICSettingActivity";
    private static final int REQUEST_CODE_DIR = 0X01;
    @Bind(R.id.btn_back)
    LinearLayout mBtnBack;
    @Bind(R.id.tv_model_name)
    TextView tvModelName;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.btn_change_3d)
    RelativeLayout mBtnChange3d;
    @Bind(R.id.img2)
    ImageView img2;
    @Bind(R.id.img3)
    ImageView img3;
    @Bind(R.id.btn_about)
    RelativeLayout mBtnAbout;
    @Bind(R.id.txt_3d)
    TextView mTxtReflect;
    @Bind(R.id.tv_cur_dir)
    TextView tvCurDir;
    @Bind(R.id.btn_file_dir)
    RelativeLayout btnFileDir;
    @Bind(R.id.btn_part3)
    RelativeLayout btnPart3;

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private String mCurReflect = "";
    private String mCurDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        ButterKnife.bind(this);

        initViews();
        initDatas();
    }

    private void initViews() {
        mTxtReflect.setText(
                getSharedPreferences(ICConstants.PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(ICConstants.KEY_REFLECT, ICConstants.ITEM_REFLECTS[0]));
    }

    private void initDatas() {
        mPreferences = getSharedPreferences(ICConstants.PREFERENCE_NAME, MODE_PRIVATE);
        mEditor = mPreferences.edit();

        //数据库中获取当前的存储路径，没有则是默认的系统相机路径
        String path = mPreferences.getString(ICConstants.KEY_IMG_DIR, FileUtils.getInst().getSystemPhotoPath());
        tvCurDir.setText(path);
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("画廊动画效果")
                .setItems(ICConstants.ITEM_REFLECTS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mEditor.putString(ICConstants.KEY_REFLECT, ICConstants.ITEM_REFLECTS[0]);
                                mEditor.putString(ICConstants.KEY_REFLECT_CUR, ICConstants.ITEM_REFLECTS[0]);
                                mEditor.commit();
                                mCurReflect = ICConstants.ITEM_REFLECTS[0];
                                mTxtReflect.setText(mCurReflect);

                                break;

                            case 1:
                                mEditor.putString(ICConstants.KEY_REFLECT, ICConstants.ITEM_REFLECTS[1]);
                                mEditor.putString(ICConstants.KEY_REFLECT_CUR, ICConstants.ITEM_REFLECTS[0]);
                                mEditor.commit();
                                mCurReflect = ICConstants.ITEM_REFLECTS[1];
                                mTxtReflect.setText(mCurReflect);

                                break;
                            default:
                                break;
                        }
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    @OnClick({R.id.btn_back, R.id.btn_change_3d, R.id.btn_about, R.id.btn_file_dir})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.btn_change_3d:
                showDialog();
                break;

            case R.id.btn_about:
                startActivity(new Intent(this, ICAboutActivity.class));
                break;

            case R.id.btn_file_dir:
//                startActivity(new Intent(this, ICFileDirActivity.class));

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, 001);

                startActivityForResult(new Intent(this, ICFileBrowserActivity.class), REQUEST_CODE_DIR);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == RESULT_OK) {
            Log.d(TAG, "data = " + data + "\n" + "data.data = " + data.getData());
        }
        if (requestCode == REQUEST_CODE_DIR && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String path = bundle.getString("savePath");
            if (!TextUtils.isEmpty(path)) {
                mCurDir = path;
                tvCurDir.setText(mCurDir);
                Log.d(TAG, "path = " + path);

                //保存用户设置
                if (mEditor != null) {
                    mEditor.putString(ICConstants.KEY_IMG_DIR, path);
                    mEditor.commit();
                }else {
                    mEditor = getSharedPreferences(ICConstants.PREFERENCE_NAME, MODE_PRIVATE).edit();
                    mEditor.putString(ICConstants.KEY_IMG_DIR, path);
                    mEditor.commit();
                }
                ToastUtil.showToast(ICSettingActivity.this, "设置成功！", Toast.LENGTH_SHORT);
            }
        }
    }

}
