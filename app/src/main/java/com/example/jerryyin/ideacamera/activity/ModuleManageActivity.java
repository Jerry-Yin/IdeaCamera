package com.example.jerryyin.ideacamera.activity;

import android.os.Bundle;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.util.CameraModelService;

import butterknife.ButterKnife;

/**
 * Created by JerryYin on 5/12/16.
 * 管理自定义模版
 * 可以添加，删除，编辑
 */
public class ModuleManageActivity extends BaseActivity {


    private CameraModelService mModelService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_module_manage);
        ButterKnife.bind(this);

    }


}
