package com.example.jerryyin.ideacamera.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jerryyin.ideacamera.base.CameraApplication;
import com.example.jerryyin.ideacamera.R;
import com.example.jerryyin.ideacamera.base.BaseActivity;
import com.example.jerryyin.ideacamera.model.PhotoItem;
import com.example.jerryyin.ideacamera.util.CameraHelper;
import com.example.jerryyin.ideacamera.util.IdeaCameraManager;
import com.example.jerryyin.ideacamera.util.common.DialogUtil;
import com.example.jerryyin.ideacamera.util.common.FileUtils;
import com.example.jerryyin.ideacamera.util.common.IOUtil;
import com.example.jerryyin.ideacamera.util.common.ImageUtils;
import com.example.jerryyin.ideacamera.util.common.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity {

    private static final String TAG = "CameraActivity";
    /**
     * 最小预览界面的分辨率
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    /**
     * 最大宽高比差
     */
    private static final double MAX_ASPECT_DISTORTION = 0.15;
    private int PHOTO_SIZE = 2000;
    private int mCurrentCameraId = 0;  //1是前置 0是后置
    private float pointX, pointY;
    static final int FOCUS = 1;            // 聚焦
    static final int ZOOM = 2;            // 缩放
    private int mode;                      //0是聚焦 1是放大
    private float dist;

    private Handler mHandler = new Handler();

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Bundle mBundle = null;
    private Camera.Parameters mParameters;
    private CameraHelper mCameraHelper;
    private Camera.Size mAdapterSize = null;
    private Camera.Size mPreviewSize = null;


    @Bind(R.id.surface_view)
    SurfaceView mSurfaceView;
    @Bind(R.id.btn_take_pic)
    FloatingActionButton mBtnTakePic;
    @Bind(R.id.img_flashBtn)
    ImageView mBtnFlash;
    @Bind(R.id.img_change)
    ImageView mBtnChange;
    @Bind(R.id.img_back)
    ImageView mBtnBack;
    @Bind(R.id.img_album)
    ImageView mBtnAlbum;
    @Bind(R.id.focus_index)
    View mFocusIndex;   //聚焦框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //应用程序的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //系统的状态栏
        setContentView(R.layout.layout_camera);
        ButterKnife.bind(this);
        mCameraHelper = new CameraHelper(this);

        initViews();
        initEvents();

    }

    private void initViews() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceView.setFocusable(true);
//        mSurfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
        mSurfaceHolder.addCallback(new SurfaceCallBack());

//        //设置相机界面,照片列表,以及拍照布局的高度(保证相机预览为正方形)
//        ViewGroup.LayoutParams layout = cameraGrid.getLayoutParams();
//        layout.height = App.getApp().getScreenWidth();
//        layout = photoArea.getLayoutParams();
//        layout.height = DistanceUtil.getCameraPhotoAreaHeight();
//        layout = takePhotoPanel.getLayoutParams();
//        layout.height = App.getApp().getScreenHeight()
//                - App.getApp().getScreenWidth()
//                - DistanceUtil.getCameraPhotoAreaHeight();

        //添加系统相册内的图片
//        ArrayList<PhotoItem> sysPhotos = FileUtils.getInst().findPicsInDir(
//                FileUtils.getInst().getSystemPhotoPath());
//        int showNumber = sysPhotos.size() > photoNumber ? photoNumber
//                : sysPhotos.size();
//        for (int i = 0; i < showNumber; i++) {
//            addPhoto(sysPhotos.get(showNumber - 1 - i));
//        }
    }

    private void initEvents() {
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // 主点按下
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        mode = FOCUS;
                        break;
                    // 副点按下
                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = spacing(event);
                        // 如果连续两点距离大于10，则判定为多点模式
                        if (spacing(event) > 10f) {
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = FOCUS;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == FOCUS) {
                            //pointFocus((int) event.getRawX(), (int) event.getRawY());
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                float tScale = (newDist - dist) / dist;
                                if (tScale < 0) {
                                    tScale = tScale * 10;
                                }
                                addZoomIn((int) tScale);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        //前后置摄像头切换
        boolean canSwitch = false;
        try {
            canSwitch = mCameraHelper.hasFrontCamera() && mCameraHelper.hasBackCamera();
        } catch (Exception e) {
            //获取相机信息失败
        }
        if (!canSwitch) {
            mBtnChange.setVisibility(View.GONE);
        } else {
            mBtnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchCamera();
                }
            });
        }
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
//        return FloatMath.sqrt(x * x + y * y);
        return (float) Math.sqrt(x * x + y * y);
    }

    int curZoomValue = 0;

    private void addZoomIn(int delta) {

        try {
            Camera.Parameters params = mCamera.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                mCamera.setParameters(params);
                return;
            } else {
                mCamera.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.surface_view, R.id.btn_take_pic, R.id.img_flashBtn, R.id.img_back, R.id.img_album})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.surface_view:
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                //聚焦框实现
                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(mFocusIndex.getLayoutParams());
                layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
                mFocusIndex.setLayoutParams(layout);
                mFocusIndex.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(800);
                mFocusIndex.startAnimation(sa);
//                mHandler.postDelayed(() -> mFocusIndex.setVisibility(View.INVISIBLE), 800);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFocusIndex.setVisibility(View.INVISIBLE);
                    }
                },
                        800);
                break;
            case R.id.btn_take_pic:
                try {
                    mCamera.takePicture(null, null, new MyPictureCallback());
                } catch (Throwable t) {
                    t.printStackTrace();
                    ToastUtil.showToast(this, "拍照失败，请重试！", Toast.LENGTH_LONG);
                    try {
                        mCamera.startPreview();
                    } catch (Throwable e) {

                    }
                }
                break;

            case R.id.img_flashBtn:
                turnLight(mCamera);
                break;

            case R.id.img_back:
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;

            case R.id.img_album:
                startActivity(new Intent(this, GalleryActivity.class));
                break;
        }
    }

    //定点对焦的代码
    private void pointFocus(int x, int y) {
        mCamera.cancelAutoFocus();
        mParameters = mCamera.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        mCamera.setParameters(mParameters);
        autoFocus();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showPoint(int x, int y) {
        if (mParameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / CameraApplication.getApp().getScreenWidth() + 1000;
            int rectX = y * 2000 / CameraApplication.getApp().getScreenHeight() - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            mParameters.setMeteringAreas(areas);
        }

        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    /**
     * 闪光灯开关   开->关->自动
     *
     * @param mCamera
     */
    private void turnLight(Camera mCamera) {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            mBtnFlash.setImageResource(R.drawable.camera_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mBtnFlash.setImageResource(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mBtnFlash.setImageResource(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mBtnFlash.setImageResource(R.drawable.camera_flash_off);
        }
    }



    /**
     * 预览界面 surfaceView 回调函数
     */
    private class SurfaceCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mCamera == null) {
                try {
                    mCamera = Camera.open();
                    mCamera.setPreviewDisplay(holder);
                    initCamera();
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            autoFocus();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (mCamera != null) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            } catch (Exception e) {
                //相机已经关了
            }
        }
    }


    private void initCamera() {
        mParameters = mCamera.getParameters();
        mParameters.setPictureFormat(PixelFormat.JPEG);
        //if (adapterSize == null) {
        setUpPicSize(mParameters);
        setUpPreviewSize(mParameters);
        //}
        if (mAdapterSize != null) {
            mParameters.setPictureSize(mAdapterSize.width, mAdapterSize.height);
        }
        if (mPreviewSize != null) {
            mParameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        } else {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        setDispaly(mParameters, mCamera);
        try {
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    public void setUpPicSize(Camera.Parameters parameters) {
        if (mAdapterSize != null) {
            return;
        } else {
            mAdapterSize = findBestPictureResolution();
            return;
        }
    }

    private void setUpPreviewSize(Camera.Parameters parameters) {

        if (mPreviewSize != null) {
            return;
        } else {
            mPreviewSize = findBestPreviewResolution();
        }
    }

    /**
     * 挑选最合适的预览界面分辨率
     *
     * @return
     */
    private Camera.Size findBestPreviewResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Camera.Size> supportedPreviewResolutions = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }
        Log.v(TAG, "Supported preview resolutions: " + previewResolutionSb);


        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) CameraApplication.getApp().getScreenWidth()
                / (double) CameraApplication.getApp().getScreenHeight();
        Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == CameraApplication.getApp().getScreenWidth()
                    && maybeFlippedHeight == CameraApplication.getApp().getScreenHeight()) {
                return supportedPreviewResolution;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        if (!supportedPreviewResolutions.isEmpty()) {
            Camera.Size largestPreview = supportedPreviewResolutions.get(0);
            return largestPreview;
        }

        // 没有找到合适的，就返回默认的

        return defaultPreviewResolution;
    }

    private Camera.Size findBestPictureResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }
        Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);

        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
        Log.d(TAG, "default picture resolution " + defaultPictureResolution.width + "x"
                + defaultPictureResolution.height);

        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) CameraApplication.getApp().getScreenWidth()
                / (double) CameraApplication.getApp().getScreenHeight();
        Iterator<Camera.Size> it = sortedSupportedPicResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        if (!sortedSupportedPicResolutions.isEmpty()) {
            return sortedSupportedPicResolutions.get(0);
        }

        // 没有找到合适的，就返回默认的
        return defaultPictureResolution;
    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);  //注意，系统的默认相机预览角度是横屏的，所以需要转动90度
        } else {
            parameters.setRotation(90);
        }
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mCamera == null) {
                    return;
                }
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }


    /**
     * 拍照回调
     */
    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mBundle = new Bundle();
            mBundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
            new SavePicTask(data).execute();
            camera.startPreview(); // 拍完照后，重新开始预览
        }
    }

    /**
     * 异步照片存储
     */
    private class SavePicTask extends AsyncTask<Void, Void, String> {
        private byte[] data;

        SavePicTask(byte[] data) {
            this.data = data;
        }

        protected void onPreExecute() {
            DialogUtil.showProgressDialog(CameraActivity.this, "处理中");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return saveToSDCard(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!TextUtils.isEmpty(result)) {
                //result-->照片保存的路径
                DialogUtil.dismissDialog();
                IdeaCameraManager.getInst().processPhotoItem(CameraActivity.this,
                        new PhotoItem(result, System.currentTimeMillis()));
            } else {
                ToastUtil.showToast(CameraActivity.this, "拍照失败，请稍后重试！", Toast.LENGTH_LONG);
            }
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    public String saveToSDCard(byte[] data) throws IOException {
        Bitmap croppedImage;
        Bitmap bmp;

        //获得图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        Log.d(TAG, "bmp.width = "+bmp.getWidth() + "height = "+bmp.getHeight());
        Matrix matrix = new Matrix();   //用于旋转的矩阵
        matrix.setRotate(90);   //旋转90度
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        BitmapFactory.decodeByteArray(data, 0, data.length, options);

//        PHOTO_SIZE = options.outHeight > options.outWidth ? options.outWidth : options.outHeight;
//        int height = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
//        options.inJustDecodeBounds = false;
//        Rect r;
//        if (mCurrentCameraId == 1) {
//            r = new Rect(height - PHOTO_SIZE, 0, height, PHOTO_SIZE);
//        } else {
//            r = new Rect(0, 0, PHOTO_SIZE, PHOTO_SIZE);
//        }
//        try {
//            croppedImage = decodeRegionCrop(data, r);
//        } catch (Exception e) {
//            return null;
//        }
//        Log.d(TAG, "bitmap.width = "+croppedImage.getWidth() + "height = "+croppedImage.getHeight());


        String imagePath = ImageUtils.saveToFile(FileUtils.getInst().getSystemPhotoPath(), true,
                bmp);  //存储到系统相册目录下
//        croppedImage.recycle();
        bmp.recycle();
        return imagePath;
    }

    private Bitmap decodeRegionCrop(byte[] data, Rect rect) {

        InputStream is = null;
        System.gc();
        Bitmap croppedImage = null;
        try {
            is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
            } catch (IllegalArgumentException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeStream(is);
        }
        Matrix m = new Matrix();
        m.setRotate(90, PHOTO_SIZE / 1, PHOTO_SIZE / 1);
        if (mCurrentCameraId == 1) {
            m.postScale(1, -1);
        }
        Bitmap rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, PHOTO_SIZE, 2000, m, true);
        if (rotatedImage != croppedImage)
            croppedImage.recycle();
        return rotatedImage;
    }

    //切换前后置摄像头
    private void switchCamera() {
        mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
        releaseCamera();
        Log.d("DDDD", "DDDD----mCurrentCameraId" + mCurrentCameraId);
        setUpCamera(mCurrentCameraId);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        mAdapterSize = null;
        mPreviewSize = null;
    }

    /**
     * @param mCurrentCameraId2
     */
    private void setUpCamera(int mCurrentCameraId2) {
        mCamera = getCameraInstance(mCurrentCameraId2);
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceView.getHolder());
                initCamera();
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast(this, "切换失败，请重试！", Toast.LENGTH_LONG);
        }
    }


    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = mCameraHelper.openCamera(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}
