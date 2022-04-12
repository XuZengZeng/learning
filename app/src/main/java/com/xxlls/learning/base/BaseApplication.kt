package com.xxlls.learning.base

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.twitter.sdk.android.core.Twitter

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //初始化Twitter
        Twitter.initialize(this)
        if (Build.VERSION.SDK_INT >= 24) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }
}