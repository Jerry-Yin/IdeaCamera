package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.btn_go_pic})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_go_pic:
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                this.finish();
                break;
        }
    }
}
