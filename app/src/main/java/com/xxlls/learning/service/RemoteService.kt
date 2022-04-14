package com.xxlls.learning.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class RemoteService : Service() {

    public val number : Int = 1

    companion object {
        const val TAG: String = "BinderSimple"
    }

    /** * 实现IRemoteService.aidl中定义的方法 */
    private val mBinder: IBinder = object : IRemoteService.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {

        }

    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "[RemoteService] onCreate")
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "[RemoteService] onUnbind")
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "[RemoteService] onBind")
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "[RemoteService] onDestroy")
    }

}