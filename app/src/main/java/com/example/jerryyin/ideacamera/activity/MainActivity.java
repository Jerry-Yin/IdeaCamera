package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 4/21/16.
 */
public class MainActivity extends BaseActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.btn_go_pic, R.id.main_part1_module_manage, R.id.main_part2_beauty, R.id.main_part3_others, R.id.main_part4_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_pic:
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                this.finish();
                break;

            case R.id.main_part1_module_manage:

                break;

            case R.id.main_part2_beauty:

                break;

            case R.id.main_part3_others:

                break;

            case R.id.main_part4_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
