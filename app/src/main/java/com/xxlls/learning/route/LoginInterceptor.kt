package com.xxlls.learning.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.xxlls.learning.utils.UserInfoUtils

/**
 * ARoute拦截器、用于登录拦截
 */
@Interceptor(priority = 8, name = "LOGIN")
class LoginInterceptor : IInterceptor {
    override fun init(context: Context?) {

    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {

        // 拦截登录标识
        if (postcard?.extra == RouteConst.LOGIN_EXTRA) {
            // 是否登录
            if (UserInfoUtils.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                // 拦截跳转
                callback!!.onInterrupt(null)
                // 自动跳转之登录页面
                ARouter.getInstance().build(RouteConst.APP_LOGIN).navigation()
            }
        } else {
            // 不拦截默认放过
            callback?.onContinue(postcard)
        }
    }
}