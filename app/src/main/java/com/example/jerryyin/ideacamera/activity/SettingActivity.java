package com.example.jerryyin.ideacamera.activity;

import android.os.Bundle;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by JerryYin on 5/12/16.
 */
public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.layout_setting);



    }
}
