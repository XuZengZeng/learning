package com.xxlls.learning

import android.app.ActionBar
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.xxlls.learning.flutter.FlutterTestActivity
import com.xxlls.learning.widget.AnimatorGroupView

class MainActivity : AppCompatActivity() {

    lateinit var bottomLayout: AnimatorGroupView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addEvent()
    }


    private fun addEvent() {
        findViewById<View>(R.id.hello_world).setOnClickListener {
            FlutterTestActivity.start(this)
        }
    }

    fun toNext(view: View) {
        bottomLayout.showNextAnimator()
    }

    fun toPre(view: View) {
        bottomLayout.showPreAnimator()
    }

    fun reSet(view: View) {
        bottomLayout.resetView()
    }

    fun startAnimationActivity(view: View) {
        AnimationActivity.start(this)
    }

}