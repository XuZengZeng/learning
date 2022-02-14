package com.xxlls.learning.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

/**
 * @Date 2/10/22
 * @Aurth xuzengzeng
 * @Desceiption 俩个子View 左右切换
 */
class AnimationChangeGroupView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    // 最大宽度
    private var childMaxWidth = 0

    // 最少的子View数量
    private val minChildCount = 2

    // 当前的展示 View的Index
    private var curIndex = 0

    // 动画是否正在执行
    private var isAnimator = false

    /**
     * 是否是安全时间
     * @return true 安全，false
     */
    // 安全执行时间，避免动画短时间内多次执行
    private var verifyTime = 1000L

    // 上一次执行动画的时间
    private var preTimeMillis = 0L
    private val animatorUpdateListener =
        ValueAnimator.AnimatorUpdateListener { animation: ValueAnimator ->
            val newHeight = animation.animatedValue as Int
            val params = layoutParams
            params.height = newHeight
            layoutParams = params
            val curView = getChildAt(curIndex)
            val layoutParams = curView.layoutParams
            if (layoutParams.height != newHeight) {
                layoutParams.height = newHeight
                curView.layoutParams = layoutParams
            }
        }
    private val animatorListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            isAnimator = true
        }

        override fun onAnimationEnd(animation: Animator) {
            isAnimator = false
        }

        override fun onAnimationCancel(animation: Animator) {
            isAnimator = false
        }

        override fun onAnimationRepeat(animation: Animator) {
            isAnimator = true
        }
    }

    private fun initView() {}
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取系统自动测量的该ViewGroup的大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var realHeightSize = 0

        // 修改了系统自动测量的子View的大小
        val childCount = this.childCount
        var childMeasuredHeight = 0
        // 只去测量一次子View
//        if (isInitChildView) {
//            for (int i = 0; i < childCount; i++) {
//                View childView = getChildAt(i);
//                // 系统自动测量子View:
//                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//            }
//            isInitChildView = false;
//        }
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            // 系统自动测量子View:
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
        }

        // 获取每个子View测量所得的宽和高
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childMeasuredHeight = childView.measuredHeight
            if (curIndex == i) {
                realHeightSize = childMeasuredHeight
            }
        }
        // 执行动画时使用动画高度
        if (isAnimator) {
            setMeasuredDimension(widthSize, heightSize)
        } else {
            // 非动画或者动画结束，使用准确高度
            setMeasuredDimension(widthSize, realHeightSize)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 所有直接子View
        val count = childCount
        if (count < minChildCount) {
            return
        }
        // 获取当前宽度
        childMaxWidth = width
        val curView = getChildAt(curIndex)
        // 获取 params 高度
        var curHeight = curView.layoutParams.height
        // params 高度获取不到时 获取 Measured 高度
        if (curHeight <= 0) {
            curHeight = curView.measuredHeight
        }

        // 摆放位置
        for (i in 0 until count) {
            val childView = getChildAt(i)
            // 左上右下
            val lift = i * childMaxWidth
            val top = measuredHeight - curHeight
            val right = lift + childMaxWidth
            val bottom = measuredHeight
            // 摆放位置
            childView.layout(lift, top, right, bottom)
        }
    }

    /**
     * 下一步
     */
    operator fun next() {
        val nextIndex = curIndex + 1
        if (isVerifyIndex(nextIndex)) {
            // 获取对应View,并执行动画
            val curView = getChildAt(curIndex)
            val nextView = getChildAt(nextIndex)
            // 开始执行动画
            if (curView != null && nextView != null && isVerifyTime()
            ) {
                nextAnimator(nextView, curView)
                curIndex++
            }
        }
    }

    /**
     * 返回
     */
    fun back() {
        val backIndex = curIndex - 1
        if (isVerifyIndex(backIndex)) {
            // 获取对应View,并执行动画
            val curView = getChildAt(curIndex)
            val nextView = getChildAt(backIndex)
            // 开始执行动画
            if (curView != null && nextView != null && isVerifyTime()
            ) {
                backAnimator(nextView, curView)
                curIndex--
            }
        }
    }

    /**
     * 开始动画
     *
     * @param nextView 下一个View
     * @param curView  当前View
     */
    private fun nextAnimator(nextView: View, curView: View) {
        val curHeight = curView.measuredHeight
        val otherHeight = nextView.measuredHeight

        // 执行nextView 的进入动画 （向左移动一个屏幕的距离，透明渐变 0 - 1）
        val translationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth.toFloat())
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        nextView.pivotY = nextView.height.toFloat()
        val nextAnimator = ObjectAnimator.ofPropertyValuesHolder(nextView, translationX, alpha)
        nextAnimator.duration = 300
        nextAnimator.interpolator = DecelerateInterpolator()
        nextAnimator.start()

        // 执行curView 的退出动画 （向左移动一个屏幕的距离，透明渐变 0 - 1）
        val curTranslationX = PropertyValuesHolder.ofFloat("translationX", -childMaxWidth.toFloat())
        val curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha)
        curAnimator.duration = 600
        curAnimator.interpolator = DecelerateInterpolator()
        curAnimator.start()

        // 操作当前控件改变高度
        val scaleYAnimator = ValueAnimator.ofInt(curHeight, otherHeight)
        scaleYAnimator.addUpdateListener(animatorUpdateListener)
        scaleYAnimator.addListener(animatorListener)
        scaleYAnimator.duration = 300
        scaleYAnimator.interpolator = DecelerateInterpolator()
        scaleYAnimator.start()
    }

    /**
     * 开始动画
     *
     * @param backView 上一个View
     * @param curView  当前View
     */
    private fun backAnimator(backView: View, curView: View) {
        val translationX = PropertyValuesHolder.ofFloat("translationX", 0f)
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val nextAnimator = ObjectAnimator.ofPropertyValuesHolder(backView, translationX, alpha)
        nextAnimator.duration = 300
        nextAnimator.interpolator = DecelerateInterpolator()
        nextAnimator.start()
        val curTranslationX = PropertyValuesHolder.ofFloat("translationX", childMaxWidth.toFloat())
        val curAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val curAnimator = ObjectAnimator.ofPropertyValuesHolder(curView, curTranslationX, curAlpha)
        curAnimator.duration = 600
        curAnimator.interpolator = DecelerateInterpolator()
        curAnimator.start()
        val curHeight = curView.measuredHeight
        val otherHeight = backView.measuredHeight

        // 操作当前控件改变高度
        val scaleYAnimator = ValueAnimator.ofInt(curHeight, otherHeight)
        scaleYAnimator.addUpdateListener(animatorUpdateListener)
        scaleYAnimator.addListener(animatorListener)
        scaleYAnimator.duration = 300
        scaleYAnimator.interpolator = DecelerateInterpolator()
        scaleYAnimator.start()
    }

    /**
     * index 是否合法
     *
     * @param index 需要检测的Index
     * @return 检测index 是否合法
     */
    private fun isVerifyIndex(index: Int): Boolean {
        return index in 0 until childCount
    }

    /**
     * 是否是安全时间,避免短时间内动画执行多次
     * @return true 安全，false
     */
    private fun isVerifyTime(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - preTimeMillis > verifyTime) {
            preTimeMillis = currentTimeMillis
            return true
        }
        return false
    }
}