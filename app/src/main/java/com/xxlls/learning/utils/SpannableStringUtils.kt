package com.xxlls.learning.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * 富文本（图文组合，字体变色处理）
 */
class SpannableStringUtils {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SpannableStringUtils() }

        const val FLAG_IMAGE = "&&"
        const val FLAG_TEXT_COLOR_START = "{{"
        const val FLAG_TEXT_COLOR_END = "}}"
    }

    /**
     * 获取对应的 SpannableString
     * {{ }} 被{{ }} 所包裹的内容将被标记颜色
     * && 被 替换为对应图片
     */
    fun getSpannableString(
        context: Context,
        content: String,
        imgRes: Int = -1,
        colorRes: Int = -1,
        boundsSize: Int = 0,
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder()
        // 查找颜色替换
        val startColorIndex = content.indexOf(FLAG_TEXT_COLOR_START)
        var endColorIndex = content.indexOf(FLAG_TEXT_COLOR_END)
        var newContent = content
        // 设置颜色
        if (endColorIndex > startColorIndex) {
            // 替换占位符
            newContent = content.replace(FLAG_TEXT_COLOR_START, "").replace(FLAG_TEXT_COLOR_END, "")
            spannable.append(newContent)
            // 开始设置颜色
            if (colorRes != -1) {
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context,colorRes)),
                    startColorIndex,
                    startColorIndex + (endColorIndex - startColorIndex - FLAG_TEXT_COLOR_END.length),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        } else {
            spannable.append(newContent)
        }

        // 查找图片替换
        val imgIndex = content.indexOf(FLAG_IMAGE)
        // 替换图片
        if (imgIndex != -1 && imgRes != -1) {
            spannable.setSpan(object : DynamicDrawableSpan(ALIGN_CENTER) {
                override fun getDrawable(): Drawable? {
                    val d = ResourcesCompat.getDrawable(context.resources, imgRes, null)
                    d?.setBounds(0,0,boundsSize,boundsSize)
                    return d
                }
            }, imgIndex, imgIndex + FLAG_IMAGE.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }


        return spannable
    }

}