package com.xxlls.learning.flutter

import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.JSONUtil
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.lang.Exception

/**
 * @Date 1/7/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
abstract class BaseFlutterActivity : FlutterActivity(), MethodCallHandler {

    companion object {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        // TODO("Not yet implemented")
        try {
            when (call.method) {
                "test" -> {
                    Toast.makeText(context, "flutter to Test", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getInitialRoute(): String {
        val config = PlatformConfig()
        config.routeName = getRouteName()
        return Gson().toJson(config)
    }


    /**
     * 获取RouterName
     */
    abstract fun getRouteName(): String

}