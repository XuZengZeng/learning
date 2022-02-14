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

    lateinit var view_3: View
    lateinit var view_2: View
    lateinit var animator_group: AnimationChangeGroupView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        initView()
        addEvent()
    }

    private fun addEvent() {
        view_2.setOnClickListener {
            if (view_3.visibility == View.VISIBLE) {
                view_3.visibility = View.GONE
            } else {
                view_3.visibility = View.VISIBLE
            }
        }

        view_3.setOnClickListener {
            if (view_2.visibility == View.VISIBLE) {
                view_2.visibility = View.GONE
            } else {
                view_2.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        animator_group = findViewById<AnimationChangeGroupView>(R.id.animator_group)
        view_2 = findViewById(R.id.view_3_2)
        view_3 = findViewById(R.id.view_3_1)
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