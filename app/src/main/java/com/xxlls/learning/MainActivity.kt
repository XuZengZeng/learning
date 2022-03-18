package com.xxlls.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xxlls.learning.flutter.FlutterTestActivity
import com.xxlls.learning.ui_test.RecyclerViewActivity

class MainActivity : AppCompatActivity() {


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

    fun startAnimationActivity(view: View) {
//        AnimationActivity.start(this)
    }


    fun startRecyclerViewActivity(view: View) {
        RecyclerViewActivity.start(this)
    }

}