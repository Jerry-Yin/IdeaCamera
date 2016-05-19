package com.example.jerryyin.ideacamera.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.adapter.NormalGalleryAdapter;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.conatants.ICConstants;
import com.example.jerryyin.ideacamera.util.ICImageHelper;
import com.example.jerryyin.ideacamera.util.common.DialogUtil;
import com.example.jerryyin.ideacamera.util.common.FileUtils;
import com.example.jerryyin.ideacamera.util.common.ImageLoaderUtils;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;
import com.example.jerryyin.ideacamera.view.CustomGallery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by JerryYin on 5/13/16.
 */
public class ICPhotoEditActivity extends ICBaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ICPhotoEditActivity";
    private static final int SAVE_OK = 0x004;
    private static final int SAVE_FAIL = 0x005;


    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;
    @Bind(R.id.layout_alpha)
    RelativeLayout layoutAlpha;
    @Bind(R.id.gallery_show)
    Gallery mGallery;
    @Bind(R.id.btn_select_pho)
    Button btnFun1;
    @Bind(R.id.btn_fun2)
    Button btnFun2;
    @Bind(R.id.btn_fun3)
    Button btnFun3;
    @Bind(R.id.btn_fun4)
    Button btnFun4;
    @Bind(R.id.image_view)
    ImageView imageView;


    /**
     * Constants
     */
    private static final int IMAGE_OPEN = 0x001;
    private static final int TAKE_PHOTO = 0x002;
    private static final int TAKE_PHOTO_THIS = 0x003;

    //    private boolean fun1State = true;
    private boolean fun2State = true;
    private boolean fun3State = true;
    private boolean fun4State = true;

    private String mCurImagePath;
    private Bitmap mCurBitmap;
    private int mCurPosition;
    //    private List<Bitmap> mBitmapLists = new ArrayList<>();      //有效果的bitmap;
    private List<Map<String, Bitmap>> mMapLists = new ArrayList<>();    //效果名字 ＋ bitmap
    private NormalGalleryAdapter mGalleryAdapter;

    private SharedPreferences mPreferences;

//    private PhotoViewAttacher mViewAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photo_edit);
        ButterKnife.bind(this);

        initViews();
        initData();
    }

    private void initViews() {
        tvTitle.setText("照片编辑");
        layoutAlpha = (RelativeLayout) findViewById(R.id.layout_alpha);
        layoutAlpha.getBackground().setAlpha(100);//0~255透明度值   0-完全透明
        tvOk.setFocusable(false);   //未改变之前不能保存操作

//        imageView.enable();
    }


    private void initData() {
//        mGalleryAdapter = new NormalGalleryAdapter(mBitmapLists, this);
        mGalleryAdapter = new NormalGalleryAdapter(mMapLists, this);
        mGallery.setAdapter(mGalleryAdapter);
        mGallery.setOnItemClickListener(this);
        mPreferences = getSharedPreferences(ICConstants.PREFERENCE_NAME, MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick({R.id.btn_back, R.id.tv_ok, R.id.btn_select_pho, R.id.btn_fun2, R.id.btn_fun3, R.id.btn_fun4, R.id.image_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.tv_ok:
                saveCurBitmap();
                break;

            case R.id.btn_select_pho:
                showSelDialog();

                break;

            case R.id.btn_fun2:
                if (fun2State) {
                    layoutAlpha.setVisibility(View.VISIBLE);
//                    showThumbFun2();   //显示缩略图

                    fun2State = false;
                } else {
                    layoutAlpha.setVisibility(View.INVISIBLE);
                    fun2State = true;
                }
                break;

            case R.id.btn_fun3:

                break;

            case R.id.btn_fun4:

                break;

            case R.id.image_view:

                break;
        }
    }

    /**
     * 保存当前图片
     */
    private void saveCurBitmap() {
        DialogUtil.showProgressDialog(this, "存储中");
        final Message message = new Message();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mCurBitmap != null) {
                    String path = mPreferences.getString(ICConstants.KEY_IMG_DIR, FileUtils.getInst().getSystemPhotoPath());
                    try {
                        imageView.setDrawingCacheEnabled(true);
                        String realPath = ImageUtils.saveToFile(path, true, imageView.getDrawingCache());
                        imageView.setDrawingCacheEnabled(false);
                        if (realPath != null){
                            message.what = SAVE_OK;
                            message.obj = realPath;
                            mHandler.sendMessage(message);

                        }

                    } catch (IOException e) {
                        message.what = SAVE_FAIL;
                        mHandler.sendMessage(message);
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_OK:
                    DialogUtil.dismissDialog();
                    ToastUtil.showToast(ICPhotoEditActivity.this, "存储完毕", Toast.LENGTH_SHORT);
                    break;

                case SAVE_FAIL:
                    DialogUtil.dismissDialog();
                    ToastUtil.showToast(ICPhotoEditActivity.this, "存储失败", Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    private void showThumbFun2() {
        // TODO: 5/18/16 显示隐藏的缩略图部分； 为list添加数据； 点击item的时候更换当前照片的效果
        mMapLists.clear();
        mGalleryAdapter.notifyDataSetChanged();
        if (mCurImagePath != null) {
//            mCurBitmap = BitmapFactory.decodeFile(mCurImagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//图片宽高都为原来的4分之一，即图片为原来的8分之一
            mCurBitmap = BitmapFactory.decodeFile(mCurImagePath, options);

            if (mCurBitmap != null) {
                mMapLists.add(addMap("原图", mCurBitmap));    //原图
                mMapLists.add(addMap("怀旧", ICImageHelper.handleOldImgEffect(mCurBitmap)));
                mMapLists.add(addMap("浮雕", ICImageHelper.handleReliefImgEffect(mCurBitmap)));
                mMapLists.add(addMap("底片", ICImageHelper.handleBottomImgEffect(mCurBitmap)));
                mGalleryAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_OPEN:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        //查询选择图片
                        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        //返回 没找到选择图片
                        if (null == cursor) {
                            return;
                        }
                        //光标移动至开头 获取图片路径
                        cursor.moveToFirst();
                        mCurImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }

                    showImageView(mCurImagePath);
                }
                break;

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
//                    Bitmap bitmap = (Bitmap) data.getParcelableExtra("data");   //像素会降低
//                    imageView.setImageBitmap(bitmap);

//                    ImageLoaderUtils.displayLocalImage(mCurImagePath, imageView, null);
                    showImageView(mCurImagePath);
                }
                break;

            case TAKE_PHOTO_THIS:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                }
                break;
            default:
                break;
        }
    }

    private void showImageView(String path) {
//        Uri uri = path.startsWith("file:") ? Uri.parse(path) : Uri.parse("file://" + path);
//        ImageUtils.asyncLoadImage(this, uri, new ImageUtils.LoadImageCallback() {
//            @Override
//            public void callback(Bitmap result) {
//                imageView.setImageBitmap(result);
//            }
//        });
        ImageLoaderUtils.displayLocalImage(path, imageView, null);
//        mViewAttacher = new PhotoViewAttacher(imageView);
        tvOk.setFocusable(false);
        showThumbFun2();
    }


    /**
     * 设置照片保存信息
     *
     * @param uri 即将要保存的照片的uri
     * @return
     */
    private Uri setSaveMessage() {
        String systemPath = FileUtils.getInst().getSystemPhotoPath();//系统相册路径
        Log.d(TAG, "systemPath = " + systemPath);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File file = new File(systemPath, filename);
        mCurImagePath = file.getPath();
        return Uri.fromFile(file);
    }

    private void showSelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片")
                .setItems(new CharSequence[]{"拍照", "系统相机拍照", "选择本地照片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                Intent intent = new Intent(ICPhotoEditActivity.this, ICCameraActivity.class);
//                                intent.putExtra("usage", ICConstants.SELECT_PHOTO);
//                                startActivityForResult(intent, TAKE_PHOTO_THIS);
                                break;

                            case 1:
                                //系统相机拍照
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri uri = setSaveMessage();  //系统相册路径
                                // 指定存储路径，这样就可以保存原图了
                                intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);     //set the image file name
                                startActivityForResult(intent1, TAKE_PHOTO);
                                break;

                            case 2:
                                //选择本地照片
                                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent2, IMAGE_OPEN);
                                break;
                        }
                    }
                })
                .create().show();
    }

    private Map<String, Bitmap> addMap(String name, Bitmap bmp) {
        Map<String, Bitmap> map = new HashMap<String, Bitmap>();
        map.put(name, bmp);
        return map;
    }

    /**
     * 效果图点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvOk.setFocusable(true);    //点击过的话可以保存
        mCurPosition = position;

        //加载list中的bitmap（分辨率会降低?）
        Map map = mMapLists.get(position);
        Iterator iterator = map.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Bitmap bitmap = (Bitmap) entry.getValue();
            imageView.setImageBitmap(bitmap);
//            mViewAttacher.update();
        }

        //加载原图
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;//图片宽高都为原来的4分之一，即图片为原来的8分之一
//        mCurBitmap = BitmapFactory.decodeFile(mCurImagePath, options);
//        imageView.setImageBitmap(mCurBitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //手动回收内存
        if (mCurBitmap != null) {
            mCurBitmap.recycle();
        }
        if (mMapLists.size() > 0) {
            for (Map<String, Bitmap> map : mMapLists) {
                Iterator iterator = map.entrySet().iterator();
                if (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    Bitmap bitmap = (Bitmap) entry.getValue();
                    bitmap.recycle();
                }
            }
        }
    }
}
