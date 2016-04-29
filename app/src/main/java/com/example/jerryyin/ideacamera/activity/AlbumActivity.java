package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.GridViewAdapter;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.model.Album;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JerryYin on 4/21/16.
 * 显示照片
 */
public class AlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.albums)
    GridView mGridView;

    private static final String TAG = "AlbumActivity";
    @Bind(R.id.tv_model_name)
    TextView mTvModelName;

    private ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
    private GridViewAdapter mGridViewAdapter;
    private Map<String, Album> albums;
    private List<String> paths = new ArrayList<String>();
    private int mCurPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_album);
        ButterKnife.bind(this);

        initData();
        setupViews();
    }

    private void initData() {
        albums = ImageUtils.findGalleries(this, paths, 0);

        Intent intent = getIntent();
        mCurPosition = intent.getIntExtra("position", 0);

        photos = albums.get(paths.get(mCurPosition)).getPhotos();
        mTvModelName.setText(albums.get(paths.get(mCurPosition)).getTitle());
    }


    private void setupViews() {
        mGridViewAdapter = new GridViewAdapter(this, photos);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(this);
    }

    //todo   打开照片－－－>>裁剪，美图等等...
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



    }
}
