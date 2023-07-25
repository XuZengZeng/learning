package com.xxlls.learning.base

import com.xxlls.learning.BuildConfig
import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.alibaba.android.arouter.launcher.ARouter
import com.twitter.sdk.android.core.Twitter

class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }
    override fun onCreate() {
        super.onCreate()

        instance = this
        //初始化Twitter
        Twitter.initialize(this)
        if (Build.VERSION.SDK_INT >= 24) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
        initRoute()
    }



    private fun initRoute() {
        ARouter.init(this)
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
    }
}