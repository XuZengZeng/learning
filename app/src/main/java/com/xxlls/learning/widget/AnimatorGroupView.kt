package com.xxlls.learning.widget

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout

/**
 * @Date 2/9/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
class AnimatorGroupView : RelativeLayout {

    private var commonWidth: Int = 0
    private var curView: View? = null
    private var nextView: View? = null
    private var preView: View? = null

    private val listener: ValueAnimator.AnimatorUpdateListener =
        ValueAnimator.AnimatorUpdateListener {
            Log.e("ValueAnimator", "animatedValue =: ${it.animatedValue}")
        }

    // 当前View 的Index
    private var curIndex: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        // 设置公共属性

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        Log.e("onFocusChanged", "childCount =: $childCount")

        // 获取所有子View,并重新设置宽度
        commonWidth = width
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val ch: Int = childView.measuredHeight
            val cw: Int = childView.measuredWidth
            val l = i * commonWidth
            val t = height - ch
            val r = l + commonWidth
            val b = height
            Log.e("layout", "cw:= $cw , ch=: $ch ,====> l=: $l , t=: $t , r=: $r , b=: $b")
            childView.layout(l, t, r, b)
        }
        // update Views
        updateViews()
    }


    private fun updateViews() {
        if (curIndex < 0 || childCount < 0) {
            preView = null
            curView = null
            nextView = null
            return
        }
        initCurView()
        initNextView()
        initPreView()
    }

    private fun initPreView() {
        if (curIndex - 1 >= 0) {
            preView = getChildAt(curIndex - 1)
            return
        }
        preView = null
    }

    private fun initNextView() {
        if (curIndex + 1 < childCount) {
            nextView = getChildAt(curIndex + 1)
            return
        }
        nextView = null
    }

    private fun initCurView() {
        if (curIndex < childCount) {
            curView = getChildAt(curIndex)
            return
        }
        curView = null
    }


    public fun showNextAnimator() {
        Log.e("showNextAnimator", "commonWidth = : $commonWidth    nextView=: $nextView")
        nextView?.let {
            openAnimator(it)
            curIndex++
            curView?.let { cur ->
                exitAnimator(cur)
            }

            updateViews()
        }
    }

    public fun showPreAnimator() {
        Log.e("showPreAnimator", "commonWidth = : $commonWidth  pre=: $preView")
        preView?.let {
            proOpenAnimator(it)
            curIndex--
            curView?.let { cur ->
                proExitAnimatorBack(cur)
            }
            updateViews()
        }
    }

    public fun resetView() {
        curIndex = 0
        updateViews()
    }

    private fun openAnimator(showView: View) {
        val transl = PropertyValuesHolder.ofFloat("translationX", -commonWidth.toFloat())
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        showView.pivotY = showView.height.toFloat()
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(showView, transl, alpha, scaleY)
            .setDuration(300)
//        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun exitAnimator(exitView: View) {
        val transl = PropertyValuesHolder.ofFloat("translationX", -commonWidth.toFloat())
        val alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(exitView, transl, alpha)
            .setDuration(300)
//        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }


    private fun proOpenAnimator(showView: View) {
        val transl = PropertyValuesHolder.ofFloat("translationX", 0f)
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(showView, transl, alpha)
            .setDuration(300)
//        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun proExitAnimatorBack(exitView: View) {
        val transl = PropertyValuesHolder.ofFloat("translationX", commonWidth.toFloat())
        val alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(exitView, transl, alpha)
            .setDuration(300)
//        animator.interpolator = DecelerateInterpolator()
        animator.start()

    }

}