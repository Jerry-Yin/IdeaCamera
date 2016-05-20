package com.example.jerryyin.ideacamera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.ICBaseActivity;
import com.example.jerryyin.ideacamera.model.CameraModel;
import com.example.jerryyin.ideacamera.util.CameraModelService;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JerryYin on 4/21/16.
 */
public class ICChooseActivity extends ICBaseActivity {

    @Bind(R.id.img_photo)
    PhotoView mImgPhoto;
    @Bind(R.id.btn_add_model)
    FloatingActionButton mBtnAddModel;
    @Bind(R.id.btn_cancle)
    Button mBtnCancle;
    @Bind(R.id.btn_save)
    Button mBtnSave;

    private static final String TAG = "ICChooseActivity";
    @Bind(R.id.btn_back)
    LinearLayout btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ok)
    TextView tvOk;

    //当前图片
    private Bitmap mCurrentBitmap;
    private Uri mCurBmpUri; //当前照片的uri
    private String mCurModel;   //将要设定的模版
    private boolean isHaveModel = false;
    private List<String> mModeNameList = new ArrayList<>(); //模版数据
//    private String[] mModeList ; //模版数据

    private CameraModelService mModelService;
    private List<CameraModel> mAllModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  //应用程序的标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //系统的状态栏
        setContentView(R.layout.layout_choose);
        ButterKnife.bind(this);

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                mCurrentBitmap = result;
                mCurBmpUri = getIntent().getData();
                mImgPhoto.setImageBitmap(mCurrentBitmap);
            }
        });

        initViews();
        initData();

    }

    private void initViews() {
        tvOk.setVisibility(View.INVISIBLE);
        tvTitle.setText("添加模版");
        mImgPhoto.enable();
    }

    private void initData() {
        Log.d(TAG, "initData() started");
        mModelService = new CameraModelService(this);
        mAllModels = mModelService.queryAllModel(); //查询当前所有的model
        if (mAllModels.size() > 0) {
            isHaveModel = true;
        } else {
            isHaveModel = false;
        }
        Log.d(TAG, "所有模版长度 ＝ " + mAllModels.size());
        for (CameraModel model : mAllModels) {
            mModeNameList.add(model.name);
            Log.d(TAG, "添加的模版 ＝ " + mModeNameList);
        }
        Log.d(TAG, "当前所有模版长度 ＝ " + mModeNameList.size());

    }


    @OnClick({R.id.btn_add_model, R.id.btn_cancle, R.id.btn_save, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_model:
                // TODO: 4/22/16 有模版就选择或者添加； 没有就添加

                View inflate = getLayoutInflater().inflate(R.layout.layout_add_model, null);
                inflate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                final EditText etModel = (EditText) inflate.findViewById(R.id.et_model);
                final ListView listModel = (ListView) inflate.findViewById(R.id.list_model);
                final AlertDialog dialog = new AlertDialog.Builder(this).
                        setTitle("添加模版")
                        .setView(inflate)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(etModel.getText())) {
                                    mCurModel = etModel.getText().toString();
//                                    savaModelToLocal(mCurModel);
                                    ToastUtil.showToast(ICChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                                } else {
                                    return;
                                }
//                                ToastUtil.showToast(ICChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mModeNameList);


                if (!TextUtils.isEmpty(mCurModel)) {
                    etModel.setText(mCurModel);
                    etModel.setSelection(mCurModel.length());
                }
                if (isHaveModel) {
                    listModel.setVisibility(View.VISIBLE);
                    listModel.setAdapter(adapter);
                } else {
                    listModel.setVisibility(View.GONE);

                }

                listModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCurModel = mModeNameList.get(position);
                        ToastUtil.showToast(ICChooseActivity.this, "当前模版：" + mCurModel, Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.btn_cancle:
                this.finish();
                break;

            case R.id.btn_back:
                this.finish();
                break;

            case R.id.btn_save:
                Intent intent = new Intent(ICChooseActivity.this, ICGalleryActivity.class);
                if (!TextUtils.isEmpty(mCurModel)) {
                    boolean saved = savaModelToLocal(mCurModel);
                    if (saved) {
                        intent.putExtra("model", mCurModel);
//                intent.putExtra("imgUri", mCurBmpUri);
                        intent.setData(mCurBmpUri);
                    } else {
                        ToastUtil.showToast(this, "模版存储失败，照片将被保存到系统相册", Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastUtil.showToast(this, "模版存储失败，照片将被保存到系统相册", Toast.LENGTH_SHORT);
                }
                startActivity(intent);
                ICChooseActivity.this.finish();
                break;
        }
    }

    /**
     * 存储model到本地数据库
     *
     * @param modelName
     */
    private boolean savaModelToLocal(String modelName) {
        boolean isSaveFinished = false;
        if (mModelService != null) {
            // TODO: 5/10/16 先判断是否已经有这个model，如果有，直接插入(更新)uri即可； 没有的话才创建一个新的model
            CameraModel model = mModelService.queryModelByName(modelName);
            if (model != null) {
//                model.imgUris.add("file://"+mCurBmpUri.toString());
                String[] values = mCurBmpUri.toString().split("\\://");
                model.imgUris.add(values[1]);
                isSaveFinished = mModelService.updateModel(model);
            } else {
                List<String> imgUris = new ArrayList<>();
//                imgUris.add("file://"+mCurBmpUri.toString());

                imgUris.add(mCurBmpUri.toString().split("\\://")[1]);
                CameraModel m = new CameraModel(modelName, imgUris);
                isSaveFinished = mModelService.insertModel(m);
            }

        }
        return isSaveFinished;
    }


}
