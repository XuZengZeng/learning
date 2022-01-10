package com.xxlls.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xxlls.learning.flutter.FlutterTestActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.hello_world).setOnClickListener {
            FlutterTestActivity.start(this)
        }

    }
}