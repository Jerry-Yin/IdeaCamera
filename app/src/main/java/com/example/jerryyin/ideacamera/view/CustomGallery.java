package com.example.jerryyin.ideacamera.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by JerryYin on 1/21/16.
 * <p>
 * 1，展示图片，系统自带Gallery组件，可以基于这个Gallery组件扩展我们所需要的效果。
 * 2，展示效果需要进行3D成像。
 * 3，展示的图片下方需要显示图片的倒影。
 * 4，展示图片的倒影需要加上“遮罩”效果。
 */
public class CustomGallery extends Gallery {

    /**
     * Gallery的中心点
     */
    private int galleryCenterPoint = 0;
    /**
     * 摄像机对象
     */
    private Camera camera;


    public CustomGallery(Context context) {
        super(context);
    }

    public CustomGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        //启动getChildStaticTransformation
        //设置为true时，允许多子类进行静态转换
        //也就是说把这个属性设成true的时候每次viewGroup(看Gallery的源码就可以看到它是从ViewGroup间
        //接继承过来的)在重新画它的child的时候都会促发getChildStaticTransformation这个函数,所以我
        //们只需要在这个函数里面去加上旋转和放大的操作就可以了
        this.setStaticTransformationsEnabled(true);
        camera = new Camera();

    }


    /**
     * 当Gallery的宽和高改变时回调此方法，第一次计算gallery的宽和高时，也会调用此方法
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        galleryCenterPoint = getGalleryCenterPoint();
    }


    /**
     * 返回gallery的item的子图形的变换效果
     *
     * @param child
     * @param t     指定当前item的变换效果
     * @return
     */
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        int viewCenterPoint = getViewCenterPoint(child); // view的中心点
        int rotateAngle = 0; // 旋转角度，默认为0

        // 如果view的中心点不等于gallery中心，两边图片需要计算旋转的角度
        if (viewCenterPoint != galleryCenterPoint) {
            // gallery中心点 - view中心点 = 差值
            int diff = galleryCenterPoint - viewCenterPoint;
            // 差值 / 图片的宽度 = 比值
            float scale = (float) diff / (float) child.getWidth();
            // 比值 * 最大旋转角度 = 最终view的旋转角度(最大旋转角度定为50度)
            rotateAngle = (int) (scale * 50);

            if (Math.abs(rotateAngle) > 50) {// 当最终旋转角度 》 最大旋转角度，要改成50或-50
                rotateAngle = rotateAngle > 0 ? 50 : -50;
            }
        }
        if (android.os.Build.VERSION.SDK_INT > 15) child.invalidate();     //4.0 以上的系统图片会变倾斜

        // 设置变换效果前，需要把Transformation中的上一个item的变换效果清除
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX); // 设置变换效果的类型为矩阵类型
        startTransformationItem((LinearLayout) child, rotateAngle, t);

        return true;

    }

    /**
     * 单个图片的方法
     * 设置变换的效果
     *
     * @param iv          gallery的item
     * @param rotateAngle 旋转的角度
     * @param t           变换的对象
     */
    private void startTransformationItem(ImageView iv, int rotateAngle, Transformation t) {
        camera.save();  //保存状态
        int absRotateAngle = Math.abs(rotateAngle);

        // 1.放大效果（中间的图片要比两边的图片大）
        camera.translate(0, 0, 100f); // 给摄像机定位
        int zoom = -250 + (absRotateAngle * 2);
        camera.translate(0, 0, zoom);

        // 2.透明度（中间的图片完全显示，两边有一定的透明度）
        int alpha = (int) (255 - (absRotateAngle * 2.5));
        iv.setAlpha(alpha);

        // 3.旋转（中间的图片没有旋转角度，只要不在中间的图片都有旋转角度）
        camera.rotateY(rotateAngle);

        Matrix matrix = t.getMatrix(); // 变换的矩阵，将变换效果添加到矩阵中
        camera.getMatrix(matrix); // 把matrix矩阵给camera对象，camera对象会把上面添加的效果转换成矩阵添加到matrix对象中
        matrix.preTranslate(-iv.getWidth() / 2, -iv.getHeight() / 2); // 矩阵前乘
        matrix.postTranslate(iv.getWidth() / 2, iv.getHeight() / 2); // 矩阵后乘

        camera.restore(); // 恢复之前保存的状态
    }

    /**
     * 一个布局
     *
     * @param iv
     * @param rotateAngle
     * @param t
     */
    private void startTransformationItem(LinearLayout iv, int rotateAngle, Transformation t) {
        camera.save();  //保存状态
        int absRotateAngle = Math.abs(rotateAngle);

        // 1.放大效果（中间的图片要比两边的图片大）
        camera.translate(0, 0, 100f); // 给摄像机定位
        int zoom = -250 + (absRotateAngle * 2);
        camera.translate(0, 0, zoom);

        // 2.透明度（中间的图片完全显示，两边有一定的透明度）
        int alpha = (int) (255 - (absRotateAngle * 2.5));
        iv.setAlpha(alpha);

        // 3.旋转（中间的图片没有旋转角度，只要不在中间的图片都有旋转角度）
        camera.rotateY(rotateAngle);

        Matrix matrix = t.getMatrix(); // 变换的矩阵，将变换效果添加到矩阵中
        camera.getMatrix(matrix); // 把matrix矩阵给camera对象，camera对象会把上面添加的效果转换成矩阵添加到matrix对象中
        matrix.preTranslate(-iv.getWidth() / 2, -iv.getHeight() / 2); // 矩阵前乘
        matrix.postTranslate(iv.getWidth() / 2, iv.getHeight() / 2); // 矩阵后乘

        camera.restore(); // 恢复之前保存的状态
    }


    /**
     * 获取item上view的中心点
     *
     * @param v
     * @return
     */
    private int getViewCenterPoint(View v) {
        return v.getWidth() / 2 + v.getLeft(); // 图片宽度的一半+图片距离屏幕左边距
    }

    /**
     * 获取Gallery的中心点
     *
     * @return
     */
    public int getGalleryCenterPoint() {
        return this.getWidth() / 2;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int keyCode;
        if (isScrollingLeft(e1, e2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode, null);
        return false;
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {

        return e2.getX() > e1.getX();

    }
}
