package com.xxlls.learning.flutter

import android.content.Context
import android.content.Intent

/**
 * @Date 1/10/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
class FlutterTestActivity : BaseFlutterActivity() {

    override fun getRouteName(): String {
        return "test_page_one"
    }

    companion object {
        public fun start(context: Context) {
            context.startActivity(Intent(context, FlutterTestActivity::class.java))
        }
    }
}