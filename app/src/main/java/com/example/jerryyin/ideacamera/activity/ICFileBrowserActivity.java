package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.ICFileDirAdapter;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ICFileBrowserActivity extends ICBaseActivity implements View.OnClickListener {

    //    @NonNull
    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    //    @Bind(R.id.tv_title)
    TextView tvTitle;
    //    @Bind(R.id.tv_ok)
    TextView tvOk;
    //    @Bind(R.id.tv_cur_dir)
    TextView tvCurDir;
    //    @Bind(R.id.list_view_dir)
    ListView listViewDir;
    //    @Bind(R.id.btn_cancel)
    Button btnCancel;
    //    @Bind(R.id.btn_select)
    Button btnSelect;
//    @Bind(R.id.btn_select2)
    FloatingActionButton btnSelect2;


    private String mRootPath = "/sdcard";   //默认的SdCard根目录
    private List<String> mItems = null;
    private List<String> mPaths = null;
    private ICFileDirAdapter mDirAdapter;
    private int mCurPosition;   //  两个作用：1.记住当前钻中的item 2.返回上一级目录时自动定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_browser);
        ButterKnife.bind(this);
//        ButterKnife.bind(this);

        initViews();
        initData();
    }

    private void initViews() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCurDir = (TextView) findViewById(R.id.tv_cur_dir);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect2 = (FloatingActionButton) findViewById(R.id.btn_select2);
        listViewDir = (ListView) findViewById(R.id.list_view_dir);
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSelect.setOnClickListener(this);
        btnSelect2.setOnClickListener(this);

        tvOk.setVisibility(View.INVISIBLE);
        tvTitle.setText("存储路径选择");
    }


    private void initData() {
        getFileDir(mRootPath);
//        mDirAdapter = new ICFileDirAdapter(this, mItems, mPaths);
    }

    private void getFileDir(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.canWrite()) {
            tvCurDir.setText(filePath);
            mItems = new ArrayList<String>();
            mPaths = new ArrayList<String>();
            File[] files = f.listFiles();
            if (!filePath.equals(mRootPath)) {
                mItems.add("goroot");
                mPaths.add(mRootPath);
                mItems.add("goparent");
                mPaths.add(f.getParent());
            }

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    mItems.add(file.getName());
                    mPaths.add(file.getPath());
                }
            }
            mDirAdapter = new ICFileDirAdapter(this, mItems, mPaths);
            listViewDir.setAdapter(mDirAdapter);
            listViewDir.setSelection(mCurPosition);
            listViewDir.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (mItems.get(position).toString().equals("goparent")) {
                        getFileDir(mPaths.get(position));
                        return;
                    } else if (mItems.get(position).toString().equals("goroot")) {
                        getFileDir(mPaths.get(position));
                        return;
                    } else {
                        mCurPosition = position;
                        File file = new File(mPaths.get(position));
                        if (file.canWrite()) {
                            if (file.isDirectory()) {
                                getFileDir(mPaths.get(position));
                            }
                        } else {
                            LinearLayout lay = new LinearLayout(ICFileBrowserActivity.this);
                            lay.setOrientation(LinearLayout.HORIZONTAL);
                            ImageView image = new ImageView(ICFileBrowserActivity.this);
                            TextView text = new TextView(ICFileBrowserActivity.this);
                            text.setTextColor(Color.RED);
                            text.setTextSize(20);
                            text.setText("很抱歉您的权限不足!");
                            Toast toast = Toast.makeText(ICFileBrowserActivity.this, text.getText().toString(), Toast.LENGTH_LONG);
                            image.setImageResource(android.R.drawable.stat_sys_warning);
                            lay.addView(image);
                            lay.addView(text);
                            toast.setView(lay);
                            toast.show();
                        }
                    }
                }
            });
        } else {
            LinearLayout lay = new LinearLayout(ICFileBrowserActivity.this);
            lay.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(ICFileBrowserActivity.this);
            TextView text = new TextView(ICFileBrowserActivity.this);
            text.setTextColor(Color.RED);
            text.setTextSize(20);
            text.setText("无SD卡,无法完成下载!");
            Toast toast = Toast.makeText(ICFileBrowserActivity.this, text.getText().toString(), Toast.LENGTH_LONG);
            image.setImageResource(android.R.drawable.stat_sys_warning);
            lay.addView(image);
            lay.addView(text);
            toast.setView(lay);
            toast.show();
            this.finish();
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_cancel, R.id.btn_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.btn_cancel:
                this.finish();
                break;

            case R.id.btn_select:
                select();
                break;

            case R.id.btn_select2:
                select();
                break;
        }
    }

    private void select() {
        /*if(mPath.getText().toString().equals(rootPath)){
                    LinearLayout lay = new LinearLayout(FileBrowserActivity.this);
                    lay.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView image = new ImageView(FileBrowserActivity.this);
                    TextView text = new TextView(FileBrowserActivity.this);
                    text.setTextColor(FileBrowserActivity.this.getResources().getColor(R.color.text_color));
                    text.setTextSize(16);
                    text.setText("很抱歉您的权限不足!");
                    Toast toast = Toast.makeText(FileBrowserActivity.this, text.getText().toString(), Toast.LENGTH_SHORT);
                    image.setImageResource(android.R.drawable.stat_sys_warning);
                    lay.addView(image);
                    lay.addView(text);
                    toast.setView(lay);
                    toast.show();
                }else{*/
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putString("savePath", tvCurDir.getText().toString());
        b.putString("url", this.getIntent().getStringExtra("url"));
        b.putString("fileName", this.getIntent().getStringExtra("fileName"));
        i.putExtras(b);
//        this.setResult(RESULT_OK, i);
        this.setResult(RESULT_OK, i);
        this.finish();
        //}
    }
}
