package com.xxlls.learning.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxlls.learning.R;

import java.math.BigDecimal;

/**
 * @Date 2/10/22
 * @Aurth xuzengzeng
 * @Desceiption 俩个子View 左右切换
 */
public class AnimationChangeGroupView extends ViewGroup {

    // 最大宽度
    private int childMaxWidth = 0;

    // 最少的子View数量
    private int minChildCount = 2;

    // 当前的展示 View的Index
    private int curIndex = 0;

    // 动画是否正在执行
    private boolean isAnimator = false;

    // 安全执行时间，避免动画短时间内多次执行
    private long verifyTime = 1000L;

    // 上一次执行动画的时间
    private long preTimeMillis = 0L;

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = animation -> {
        int newHeight = (int) animation.getAnimatedValue();
        LayoutParams params = getLayoutParams();
        params.height = newHeight;
        setLayoutParams(params);
        View curView = getChildAt(curIndex);
        LayoutParams layoutParams = curView.getLayoutParams();
        if (layoutParams.height != newHeight) {
            layoutParams.height = newHeight;
            curView.setLayoutParams(layoutParams);
        }
    };

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isAnimator = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimator = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimator = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            isAnimator = true;
        }
    };


    public AnimationChangeGroupView(Context context) {
        this(context, null);
    }

    public AnimationChangeGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationChangeGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取系统自动测量的该ViewGroup的大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int realHeightSize = 0;

        // 修改了系统自动测量的子View的大小
        int childCount = this.getChildCount();
        int childMeasuredHeight = 0;
        // 只去测量一次子View
//        if (isInitChildView) {
//            for (int i = 0; i < childCount; i++) {
//                View childView = getChildAt(i);
//                // 系统自动测量子View:
//                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//            }
//            isInitChildView = false;
//        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 系统自动测量子View:
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }

        // 获取每个子View测量所得的宽和高
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childMeasuredHeight = childView.getMeasuredHeight();
            if (curIndex == i) {
                realHeightSize = childMeasuredHeight;
            }
        }
        // 执行动画时使用动画高度
        if (isAnimator) {
            setMeasuredDimension(widthSize, heightSize);
        } else {
            // 非动画或者动画结束，使用准确高度
            setMeasuredDimension(widthSize, realHeightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 所有直接子View
        int count = getChildCount();
        if (count < minChildCount) {
            return;
        }
        // 获取当前宽度
        childMaxWidth = getWidth();
        View curView = getChildAt(curIndex);
        // 获取 params 高度
        int curHeight = curView.getLayoutParams().height;
        // params 高度获取不到时 获取 Measured 高度
        if (curHeight <= 0) {
            curHeight = curView.getMeasuredHeight();
        }

        // 摆放位置
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            // 左上右下
            int lift = i * childMaxWidth;
            int top = getMeasuredHeight() - curHeight;
            int right = lift + childMaxWidth;
            int bottom = getMeasuredHeight();
            // 摆放位置
            childView.layout(lift, top, right, bottom);
        }
    }

    /**
     * 下一步
     */
    public void next() {
        int nextIndex = curIndex + 1;
        if (isVerifyIndex(nextIndex)) {
            // 获取对应View,并执行动画
            View curView = getChildAt(curIndex);
            View nextView = getChildAt(nextIndex);
            // 开始执行动画
            if (curView != null
                    && nextView != null
                    && isVerifyTime()) {
                nextAnimator(nextView, curView);
                curIndex++;
            }
        }
    }

    /**
     * 返回
     */
    public void back() {
        int backIndex = curIndex - 1;
        if (isVerifyIndex(backIndex)) {
            // 获取对应View,并执行动画
            View curView = getChildAt(curIndex);
            View nextView = getChildAt(backIndex);
            // 开始执行动画
            if (curView != null
                    && nextView != null
                    && isVerifyTime()) {
                backAnimator(nextView, curView);
                curIndex--;
            }
        }
    }


    /**
     * 开始动画
     *
     * @param nextView 下一个View
     * @param curView  当前View
     */
    private void nextAnimator(View nextView, View curView) {


        int curHeight = curView.getMeasuredHeight();
        int otherHeight = nextView.getMeasuredHeight();

        // 执行nextView 的进入动画 （向左移动一个屏幕的距离，透明渐变 0 - 1）
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        nextView.setPivotY(nextView.getHeight());
        ObjectAnimator nextAnimator = ObjectAnimator.ofPropertyValuesHolder(nextView, translationX, alpha);
        nextAnimator.setDuration(300);
        nextAnimator.setInterpolator(new DecelerateInterpolator());
        nextAnimator.start();

        // 执行curView 的退出动画 （向左移动一个屏幕的距离，透明渐变 0 - 1）
        PropertyValuesHolder curTranslationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth);
        PropertyValuesHolder curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha);
        curAnimator.setDuration(600);
        curAnimator.setInterpolator(new DecelerateInterpolator());
        curAnimator.start();

        // 操作当前控件改变高度
        ValueAnimator scaleYAnimator = ValueAnimator.ofInt(curHeight, otherHeight);
        scaleYAnimator.addUpdateListener(animatorUpdateListener);
        scaleYAnimator.addListener(animatorListener);
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());
        scaleYAnimator.start();

    }

    /**
     * 开始动画
     *
     * @param backView 上一个View
     * @param curView  当前View
     */
    private void backAnimator(View backView, View curView) {
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator nextAnimator = ObjectAnimator.ofPropertyValuesHolder(backView, translationX, alpha);
        nextAnimator.setDuration(300);
        nextAnimator.setInterpolator(new DecelerateInterpolator());
        nextAnimator.start();


        PropertyValuesHolder curTranslationX = PropertyValuesHolder.ofFloat("translationX", childMaxWidth);
        PropertyValuesHolder curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha);
        curAnimator.setDuration(600);
        curAnimator.setInterpolator(new DecelerateInterpolator());
        curAnimator.start();

        int curHeight = curView.getMeasuredHeight();
        int otherHeight = backView.getMeasuredHeight();

        // 操作当前控件改变高度
        ValueAnimator scaleYAnimator = ValueAnimator.ofInt(curHeight, otherHeight);
        scaleYAnimator.addUpdateListener(animatorUpdateListener);
        scaleYAnimator.addListener(animatorListener);
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());
        scaleYAnimator.start();
    }


    /**
     * index 是否合法
     *
     * @param index 需要检测的Index
     * @return 检测index 是否合法
     */
    private boolean isVerifyIndex(int index) {
        return index < getChildCount() && index >= 0;
    }

    /**
     * 是否是安全时间
     * @return true 安全，false
     */
    private boolean isVerifyTime() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - preTimeMillis > verifyTime) {
            preTimeMillis = currentTimeMillis;
            return true;
        }
        return false;
    }

}
