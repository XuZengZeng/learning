package com.xxlls.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.xxlls.learning.flutter.FlutterTestActivity
import com.xxlls.learning.route.RouteConst
import com.xxlls.learning.route.RouteHelper
import com.xxlls.learning.ui_test.RecyclerViewActivity
import com.xxlls.learning.ui_test.ShareActivity

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
        RouteHelper.linkJump(this, RouteConst.getInnerRouteUrl(RouteConst.APP_ANIMATION))
    }


    fun startRecyclerViewActivity(view: View) {
        RecyclerViewActivity.start(this)
    }

    /**
     * 跳转分享
     */
    fun startShareActivity(view: View) {
        ShareActivity.start(this)
    }

    fun openVerifyActivity(view: View) {
        ARouter.getInstance().build(RouteConst.APP_VERIFY).navigation()
    }

}