package com.xxlls.learning.ui_test

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Date 3/7/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
class SingleItemDecoration : RecyclerView.ItemDecoration() {

    /**
     * 分割线高度
     */
    private val mDividerHeight: Int = 10

    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.RED
    }

    /**
     * 控制 Item 偏移
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount
        // 第一个不做偏移处理
        if (position != 0) {
            outRect.top = mDividerHeight
        }
    }

    /**
     * 通过此方法绘制的任何内容都将在绘制项目视图之前绘制，因此将出现在视图下方。
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDraw(c, parent, state)



    }


    /**
     * 通过此方法绘制的任何内容都将在绘制项目视图之后绘制，
     * 因此将出现在视图之上。
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
// 获取layoutManager 用于获取 getItemOffsets 所偏移出的距离
        val layoutManager = parent.layoutManager!!
        // 绘制内容
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            // 过滤第一个 Item , 不需要绘制第一个
            if (i == 0) {
                continue
            }
            // 获取当前Item
            val childView = parent.getChildAt(i)
            val l = 0f
            val t = childView.top - mDividerHeight
            val r = childView.measuredWidth
            val b = childView.top
            c.drawRect(l, t.toFloat(), r.toFloat(), b.toFloat(), mPaint)
        }
    }


}