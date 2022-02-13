package com.xxlls.learning

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xxlls.learning.widget.AnimationChangeGroupView
import com.xxlls.learning.widget.AnimatorGroupView

/**
 * @Date 2/9/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
class AnimationActivity : AppCompatActivity() {

    lateinit var view_200: View
    lateinit var view_300: View
    lateinit var animator_group: AnimationChangeGroupView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        initView()
    }

    private fun initView() {
        animator_group = findViewById<AnimationChangeGroupView>(R.id.animator_group)
    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AnimationActivity::class.java)
            context.startActivity(starter)
        }
    }

    fun toNext(view: View) {
        animator_group.next()
    }

    fun toPre(view: View) {
        animator_group.back()
    }
}