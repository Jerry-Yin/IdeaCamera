package com.example.jerryyin.ideacamera.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JerryYin on 4/23/16.
 */
public class ShowPhoActivity extends BaseActivity {


    @Bind(R.id.img_show)
    ImageView mImgShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_img);
        ButterKnife.bind(this);

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
//                mCurrentBitmap = result;
//                mCurBmpUri = getIntent().getData();
                mImgShow.setImageBitmap(result);
            }
        });
    }
}
