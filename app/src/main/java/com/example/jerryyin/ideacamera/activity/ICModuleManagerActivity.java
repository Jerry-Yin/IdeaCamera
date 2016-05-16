package com.example.jerryyin.ideacamera.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.ModuleListAdapter;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.model.CameraModel;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;
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
public class ICModuleManagerActivity extends ICBaseActivity implements CustomListView.ItemClickListener {


    @Bind(R.id.btn_back)
    LinearLayout mBtnBack;
    @Bind(R.id.list_module)
    CustomListView mLvModule;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;

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
        tvTitle.setText("模版管理");
        tvOk.setVisibility(View.INVISIBLE);
    }


    private void initData() {
        mModelService = new CameraModelService(this);
        mModelLists = mModelService.queryAllModel();
        addDataToList();
//        addTestData();
        mArrayAdapter = new ModuleListAdapter(this, mModuleNameList);
        mLvModule.setAdapter(mArrayAdapter);
        mLvModule.setOnItemClickListener(this);

    }

    private void addTestData() {
        for (int i = 0; i < 20; i++) {
            mModuleNameList.add("测试数据 " + i);
        }
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


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.showToast(this, "clicked item"+ position, Toast.LENGTH_SHORT);
//
//    }

    @Override
    public void onItemClick(int position) {
        ToastUtil.showToast(this, "clicked item" + position, Toast.LENGTH_SHORT);
    }
}
