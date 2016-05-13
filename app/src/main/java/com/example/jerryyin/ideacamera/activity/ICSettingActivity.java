package com.example.jerryyin.ideacamera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.base.ICConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 5/12/16.
 */
public class ICSettingActivity extends ICBaseActivity {

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
    private String mCurReflect = "";


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
        mEditor = getSharedPreferences(ICConstants.PREFERENCE_NAME, MODE_PRIVATE).edit();

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
                startActivity(new Intent(this, ICFileDirActivity.class));
                break;
        }
    }

}
