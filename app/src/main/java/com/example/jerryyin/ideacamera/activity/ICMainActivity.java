package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 4/21/16.
 */
public class ICMainActivity extends ICBaseActivity {

    @NonNull
    @Bind(R.id.btn_go_pic)
    FloatingActionButton mBtnGoPic;
    @Bind(R.id.main_part1_module_manage)
    LinearLayout mainPart1ModuleManage;
    @Bind(R.id.main_part2_beauty)
    LinearLayout mainPart2Beauty;
    @Bind(R.id.main_part3_others)
    LinearLayout mainPart3Others;
    @Bind(R.id.main_part4_setting)
    LinearLayout mainPart4Setting;
    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        tvTitle.setText(R.string.app_name);
        btnBack.setVisibility(View.INVISIBLE);
        tvOk.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.btn_go_pic, R.id.main_part1_module_manage, R.id.main_part2_beauty, R.id.main_part3_others, R.id.main_part4_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_pic:
                startActivity(new Intent(ICMainActivity.this, ICCameraActivity.class));
//                this.finish();
                break;

            case R.id.main_part1_module_manage:
                startActivity(new Intent(this, ICModuleManagerActivity.class));
                break;

            case R.id.main_part2_beauty:
                startActivity(new Intent(this, ICPhotoEditActivity.class));
                break;

            case R.id.main_part3_others:
                startActivity(new Intent(this, ICInterestingActivity.class));
                break;

            case R.id.main_part4_setting:
                startActivity(new Intent(this, ICSettingActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageView img = (ImageView) findViewById(R.id.buttom_bar_black);
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        drawable.getBitmap().recycle();
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_title);
//        bitmap.recycle();

    }
}
