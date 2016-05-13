package com.example.jerryyin.ideacamera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.CusGalleryAdapter;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.model.Album;
import com.example.jerryyin.ideacamera.model.CameraModel;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.StringUtils;
import com.example.jerryyin.ideacamera.view.CustomGallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JerryYin on 4/22/16.
 */
public class ICGalleryActivity extends ICBaseActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.gallery)
    CustomGallery mGallery;
//    private GalleryFlow mGallery;

    private CusGalleryAdapter mGalleryAdapter;
    private List<String> mListModelNames = new ArrayList<>();
    private List<Integer> mListIcons = new ArrayList<>();

    private Uri mImgUri;

    //获取所有的相册
    private Map<String, Album> mAlbumMaps;
    private List<String> mAlbumPaths = new ArrayList<String>();
    private List<List<PhotoItem>> mPhotoItems = new ArrayList<>();  //每个相册中的第一张照片

    private CameraModelService mModelService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gallery);
        ButterKnife.bind(this);

        initData();
        setupView();
    }

    private void initData() {
        mAlbumMaps = ImageUtils.findGalleries(this, mAlbumPaths, 0);
        if (mAlbumPaths.size() >= 0) {
            addGalleryItem();
        }

        mModelService = new CameraModelService(this);
        List<CameraModel> modelList = mModelService.queryAllModel();
        if (modelList.size()>0){
            addModelItem(modelList);
        }
    }

    //根据系统相册内容数目 添加 画廊数目 （图标＋名字）
    private void addGalleryItem() {
        for (int i = 0; i < mAlbumPaths.size(); i++) {
            Album album = mAlbumMaps.get(mAlbumPaths.get(i));
            mPhotoItems.add(album.getPhotos());
            String name = album.getTitle();

//            mListIcons.add(R.drawable.ic_wallpaper_white_24dp);    //默认图标
            mListModelNames.add(name);

        }
    }


    /**
     * 添加model中的内容
     * @param modelList
     */
    private void addModelItem(List<CameraModel> modelList) {
        for (CameraModel model:modelList){
            mListModelNames.add(model.name);
            List<String> imgUris = model.imgUris;
            PhotoItem photoItem = null;
            List<PhotoItem> photoItemList = new ArrayList<>();
            for (String uri: imgUris){
                photoItem = new PhotoItem(StringUtils.cutStringNull(uri));
                photoItemList.add(photoItem);
            }
            mPhotoItems.add(photoItemList);
        }
    }

    private void setupView() {
        mGallery = (CustomGallery) findViewById(R.id.gallery);
        mGalleryAdapter = new CusGalleryAdapter(this, mPhotoItems, mListModelNames);
        mGallery.setAdapter(mGalleryAdapter);
        mGallery.setOnItemClickListener(this);
//        mGalleryAdapter.getView()
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ICGalleryActivity.this, ICAlbumActivity.class);
        LinearLayout layout = (LinearLayout) view;
        TextView name = (TextView) layout.getChildAt(1);
        intent.putExtra("title", name.getText());
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
