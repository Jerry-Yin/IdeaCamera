package com.example.jerryyin.ideacamera.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.ModuleListAdapter;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.model.CameraModel;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.view.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 5/13/16.
 * 管理自定义模版
 * 可以添加，删除，编辑
 */
public class ModuleManagerActivity extends BaseActivity {


    @Bind(R.id.btn_back)
    LinearLayout mBtnBack;
    @Bind(R.id.list_module)
    CustomListView mLvModule;

    private CameraModelService mModelService;
    private List<CameraModel> mModelLists;
    private List<String> mModuleNameList = new ArrayList<>();
    private ModuleListAdapter mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_module_manage);
        ButterKnife.bind(this);

        initViews();
        initData();

    }

    private void initViews() {

    }


    private void initData() {
        mModelService = new CameraModelService(this);
        mModelLists = mModelService.queryAllModel();
        addDataToList();
        mArrayAdapter = new ModuleListAdapter(this, mModuleNameList);
        mLvModule.setAdapter(mArrayAdapter);

    }

    private void addDataToList() {
        if (mModelLists.size() > 0) {
            for (CameraModel model : mModelLists) {
                mModuleNameList.add(model.name);
            }
        }
    }


    @OnClick(R.id.btn_back)
    public void onClick() {

    }



}
