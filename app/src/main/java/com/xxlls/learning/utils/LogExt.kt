package com.xxlls.learning.utils

import android.os.Build
import android.util.Log
import com.twitter.sdk.android.BuildConfig

const val TAG = "xxlls log:"

/**
 * 是否需要开启打印日志（统一日志打印）
 * 根据true|false 控制网络请求日志和该框架产生的打印
 */
var openLog = BuildConfig.DEBUG

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logv(tag: String = TAG) =
    log(LEVEL.V, tag, this)
fun String.logd(tag: String = TAG) =
    log(LEVEL.D, tag, this)
fun String.logi(tag: String = TAG) =
    log(LEVEL.I, tag, this)
fun String.logw(tag: String = TAG) =
    log(LEVEL.W, tag, this)
fun String.loge(tag: String = TAG) =
    log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, message: String) {
    if (openLog) return
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}