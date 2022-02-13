package com.xxlls.learning.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @Date 2/10/22
 * @Aurth xuzengzeng
 * @Desceiption 俩个子View 左右切换
 */
public class AnimationChangeGroupView extends RelativeLayout {

    // 最大宽度
    private int childMaxWidth = 0;

    // 最少的子View数量
    private int minChildCount = 2;

    // 当前的展示 View的Index
    private int curIndex = 0;

    public AnimationChangeGroupView(Context context) {
        this(context, null);
    }

    public AnimationChangeGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationChangeGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化公共属性
        setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 所有直接子View
        int count = getChildCount();
        if (count < minChildCount) {
            super.onLayout(changed, l, t, r, b);
            return;
        }
        // 获取当前宽度
        childMaxWidth = getWidth();
        // 摆放位置
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int ch = childView.getMeasuredHeight();
            // 左上右下
            int lift = i * childMaxWidth;
            int top = getHeight() - ch;
            int right = lift + childMaxWidth;
            int bottom = getHeight();
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
            if (curView != null && nextView != null) {
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
            if (curView != null && nextView != null) {
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
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        nextView.setPivotY(nextView.getHeight());
        ObjectAnimator nextAnimator = ObjectAnimator.ofPropertyValuesHolder(nextView, translationX, alpha, scaleY);
        nextAnimator.setDuration(300);
        nextAnimator.start();


        PropertyValuesHolder curTranslationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth);
        PropertyValuesHolder curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha);
        curAnimator.setDuration(300);
        curAnimator.start();
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
        nextAnimator.start();


        PropertyValuesHolder curTranslationX = PropertyValuesHolder.ofFloat("translationX", childMaxWidth);
        PropertyValuesHolder curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha);
        curAnimator.setDuration(300);
        curAnimator.start();
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


}
