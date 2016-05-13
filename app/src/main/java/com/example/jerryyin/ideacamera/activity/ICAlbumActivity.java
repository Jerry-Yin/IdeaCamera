package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.GridViewAdapter;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.model.Album;
import com.example.jerryyin.ideacamera.model.CameraModel;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 4/21/16.
 * 显示照片
 */
public class ICAlbumActivity extends ICBaseActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.albums)
    GridView mGridView;

    private static final String TAG = "ICAlbumActivity";
//    @Bind(R.id.tv_model_name)
//    TextView mTvModelName;
    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;

    private ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
    private GridViewAdapter mGridViewAdapter;
    private Map<String, Album> albums;
    private List<String> paths = new ArrayList<String>();
    private int mCurPosition;
    private String mCurTitle;

    //查询数据库的参数
    private CameraModelService mModelService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_album);
        ButterKnife.bind(this);

        initData();
        setupViews();
    }

    private void initData() {
        Intent intent = getIntent();
        mCurPosition = intent.getIntExtra("position", 0);
        mCurTitle = intent.getStringExtra("title");

        mModelService = new CameraModelService(this);
        CameraModel model = mModelService.queryModelByName(mCurTitle);
        if (model != null) {
            //模版
            List<String> imgUris = model.imgUris;
            PhotoItem photoItem = null;
            for (String uri : imgUris) {
                photoItem = new PhotoItem(StringUtils.cutStringNull(uri));
                photos.add(photoItem);
            }
        } else {
            //系统
            albums = ImageUtils.findGalleries(this, paths, 0);
            photos = albums.get(paths.get(mCurPosition)).getPhotos();

        }
    }

    private void setupViews() {
//        mTvModelName.setText(albums.get(paths.get(mCurPosition)).getTitle());
        tvTitle.setText(mCurTitle);
        mGridViewAdapter = new GridViewAdapter(this, photos);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(this);
    }

    //todo   打开照片－－－>>裁剪，美图等等...
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        this.finish();
    }
}
