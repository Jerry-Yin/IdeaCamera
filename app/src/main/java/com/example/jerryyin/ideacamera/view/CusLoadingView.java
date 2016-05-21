//package com.example.jerryyin.ideacamera.view;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.mingle.widget.LoadingView;
//import com.mingle.widget.ShapeLoadingView;
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.AnimatorSet;
//import com.nineoldandroids.animation.ObjectAnimator;
//
///**
// * Created by JerryYin on 5/20/16.
// */
//public class CusLoadingView extends FrameLayout {
//
//
//    private static final int ANIMATION_DURATION = 500;
//    private static float mDistance = 200.0F;
//    private ShapeLoadingView mShapeLoadingView;
//    private ImageView mIndicationIm;
//    private TextView mLoadTextView;
//    private int mTextAppearance;
//    private String mLoadText;
//    private AnimatorSet mAnimatorSet = null;
//    private Runnable mFreeFallRunnable = new Runnable() {
//        public void run() {
//            CusLoadingView.this.freeFall();
//        }
//    };
//    public float factor = 1.2F;
//
//    public CusLoadingView(Context context) {
//        super(context);
//    }
//
//    public CusLoadingView(Context context, AttributeSet attrs) {
//        super(context, attrs, 0);
//        this.init(context, attrs);
//    }
//
//    private void init(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.mingle.shapeloading.R.styleable.LoadingView);
//        this.mLoadText = typedArray.getString(com.mingle.shapeloading.R.styleable.LoadingView_loadingText);
//        this.mTextAppearance = typedArray.getResourceId(com.mingle.shapeloading.R.styleable.LoadingView_loadingTextAppearance, -1);
//        typedArray.recycle();
//    }
//
//    public CusLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.init(context, attrs);
//    }
//
//    @TargetApi(21)
//    public CusLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        this.init(context, attrs);
//    }
//
//    public int dip2px(float dipValue) {
//        float scale = this.getContext().getResources().getDisplayMetrics().density;
//        return (int)(dipValue * scale + 0.5F);
//    }
//
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        View view = LayoutInflater.from(this.getContext()).inflate(com.mingle.shapeloading.R.layout.load_view, (ViewGroup)null);
//        mDistance = (float)this.dip2px(54.0F);
//        LayoutParams layoutParams = new LayoutParams(-2, -2);
//        layoutParams.gravity = 17;
//        this.mShapeLoadingView = (ShapeLoadingView)view.findViewById(com.mingle.shapeloading.R.id.shapeLoadingView);
//        this.mIndicationIm = (ImageView)view.findViewById(com.mingle.shapeloading.R.id.indication);
//        this.mLoadTextView = (TextView)view.findViewById(com.mingle.shapeloading.R.id.promptTV);
//        if(this.mTextAppearance != -1) {
//            this.mLoadTextView.setTextAppearance(this.getContext(), this.mTextAppearance);
//        }
//
//        this.setLoadingText(this.mLoadText);
//        this.addView(view, layoutParams);
//        this.startLoading(900L);
//    }
//
//    private void startLoading(long delay) {
//        if(this.mAnimatorSet == null || !this.mAnimatorSet.isRunning()) {
//            this.removeCallbacks(this.mFreeFallRunnable);
//            if(delay > 0L) {
//                this.postDelayed(this.mFreeFallRunnable, delay);
//            } else {
//                this.post(this.mFreeFallRunnable);
//            }
//
//        }
//    }
//
//    private void stopLoading() {
//        if(this.mAnimatorSet != null) {
//            if(this.mAnimatorSet.isRunning()) {
//                this.mAnimatorSet.cancel();
//            }
//
//            this.mAnimatorSet = null;
//        }
//
//        this.removeCallbacks(this.mFreeFallRunnable);
//    }
//
//    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
//        if(visibility == VISIBLE) {
//            this.startLoading(200L);
//        } else {
//            this.stopLoading();
//        }
//
//    }
//
//    public void setLoadingText(CharSequence loadingText) {
//        if(TextUtils.isEmpty(loadingText)) {
//            this.mLoadTextView.setVisibility(GONE);
//        } else {
//            this.mLoadTextView.setVisibility(VISIBLE);
//        }
//
//        this.mLoadTextView.setText(loadingText);
//    }
//
//    public void upThrow() {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.mShapeLoadingView, "translationY", new float[]{mDistance, 0.0F});
//        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(this.mIndicationIm, "scaleX", new float[]{0.2F, 1.0F});
//        ObjectAnimator objectAnimator1 = null;
//        switch(CusLoadingView.SyntheticClass_1.$SwitchMap$com$mingle$widget$ShapeLoadingView$Shape[this.mShapeLoadingView.getShape().ordinal()]) {
//            case 1:
//                objectAnimator1 = ObjectAnimator.ofFloat(this.mShapeLoadingView, "rotation", new float[]{0.0F, -120.0F});
//                break;
//            case 2:
//                objectAnimator1 = ObjectAnimator.ofFloat(this.mShapeLoadingView, "rotation", new float[]{0.0F, 180.0F});
//                break;
//            case 3:
//                objectAnimator1 = ObjectAnimator.ofFloat(this.mShapeLoadingView, "rotation", new float[]{0.0F, 180.0F});
//        }
//
//        objectAnimator.setDuration(500L);
//        objectAnimator1.setDuration(500L);
//        objectAnimator.setInterpolator(new DecelerateInterpolator(this.factor));
//        objectAnimator1.setInterpolator(new DecelerateInterpolator(this.factor));
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(500L);
//        animatorSet.playTogether(new Animator[]{objectAnimator, objectAnimator1, scaleIndication});
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            public void onAnimationStart(Animator animation) {
//            }
//
//            public void onAnimationEnd(Animator animation) {
//                CusLoadingView.this.freeFall();
//            }
//
//            public void onAnimationCancel(Animator animation) {
//            }
//
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//        animatorSet.start();
//    }
//
//    public void freeFall() {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.mShapeLoadingView, "translationY", new float[]{0.0F, mDistance});
//        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(this.mIndicationIm, "scaleX", new float[]{1.0F, 0.2F});
//        objectAnimator.setDuration(500L);
//        objectAnimator.setInterpolator(new AccelerateInterpolator(this.factor));
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(500L);
//        animatorSet.playTogether(new Animator[]{objectAnimator, scaleIndication});
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            public void onAnimationStart(Animator animation) {
//            }
//
//            public void onAnimationEnd(Animator animation) {
//                CusLoadingView.this.mShapeLoadingView.changeShape();
//                CusLoadingView.this.upThrow();
//            }
//
//            public void onAnimationCancel(Animator animation) {
//            }
//
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//        animatorSet.start();
//    }
//
//}
