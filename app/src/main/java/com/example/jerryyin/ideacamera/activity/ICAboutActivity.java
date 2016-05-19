package com.example.jerryyin.ideacamera.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 5/13/16.
 */
public class ICAboutActivity extends ICBaseActivity {


    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_ic);
        ButterKnife.bind(this);


        initViews();
    }

    private void initViews() {
        tvTitle.setText("关于");
        tvOk.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        this.finish();
    }
}
