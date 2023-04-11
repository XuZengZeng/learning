package com.xxlls.learning.route

/**
 * 路由名称统一管理类(所有路由名称需要在此处声明)
 * @author Arthur
 */
class RouteConst {
    companion object {

        /**
         * 外部协议头
         */
        const val HTTP: String = "http://"
        const val HTTPS: String = "https://"

        /**
         * 内链协议头
         */
        const val INNER_AGREEMENT_HEADER = "inner://"

        /**
         * 内链基础协议地址
         */
        const val BASE_INNER_URL = "inner://xxlls.xyz"


        /**
         * 基础包空间（方便后期模块化扩展）
         */
        private const val BASE_GROUP_NAME = "/xxlls"

        /**
         * 登录页面
         */
        const val APP_LOGIN: String = "$BASE_GROUP_NAME/home/login"

        /**
         * 跟单首页
         */
        const val APP_FOLLOW_HOME: String = "$BASE_GROUP_NAME/home/follow_home"

        /**
         * 动画页面
         */
        const val APP_ANIMATION: String = "$BASE_GROUP_NAME/home/animation"


        /**
         * 登录拦截
         */
        const val LOGIN_EXTRA: Int = 100

        /**
         * 根据路由名称获取内链地址
         * @param routeName 路由名称 ${RouteConst.APP_LOGIN}
         */
        fun getInnerRouteUrl(routeName: String): String {
            return "${BASE_INNER_URL}${routeName}"
        }
    }
}