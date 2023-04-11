package com.xxlls.learning.route

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.xxlls.learning.utils.loge
import com.xxlls.learning.web.WebActivity

/**
 * 跳转配置，支持指定协议跳转内部页面
 * 内部协议：RouteConst.INNER_AGREEMENT_HEADER
 * 外部协议：RouteConst.BASE_INNER_URL
 */
class RouteHelper {

    companion object {
        /**
         * 链接跳转
         * 区分内链、外链
         */
        fun linkJump(context: Context, jumpUrl: String) {
            // 区分内外链协议
            if (jumpUrl.startsWith(RouteConst.INNER_AGREEMENT_HEADER)) {
                analysisInner(jumpUrl)
            } else if (jumpUrl.startsWith(RouteConst.HTTP) || jumpUrl.startsWith(RouteConst.HTTPS)) {
                // http | https 使用（目前通过WebView直接开Url地址，后续需要扩展）
                outerJump(context, jumpUrl)
            } else {
                // 异常处理
                errorLog(jumpUrl)
            }

        }

        /**
         * 解析内链 url
         * inner://openex.xyz
         */
        private fun analysisInner(jumpUrl: String) {
            // 验证内链是否有效
            if (jumpUrl.contains(RouteConst.BASE_INNER_URL)) {
                var indexOf = jumpUrl.indexOf("?")
                if (indexOf == -1) {
                    indexOf = jumpUrl.length
                }
                // 获取RouteName
                val routerName = jumpUrl.substring(RouteConst.BASE_INNER_URL.length, indexOf)
                // 解析参数
                val params = analysisParams(jumpUrl)
                innerJump(routerName, params)
            } else {
                errorLog(jumpUrl)
            }
        }

        /**
         * 解析Url地址的参数
         * @param jumpUrl 跳转地址
         */
        private fun analysisParams(jumpUrl: String): MutableMap<String, String> {
            val indexOf = jumpUrl.indexOf("?")
            val params = mutableMapOf<String, String>()
            if (indexOf != -1) {
                // 获取参数部分
                val paramsUrl = jumpUrl.substring(indexOf + 1, jumpUrl.length)
                // 解析参数
                val map = if (paramsUrl.contains("&")) {
                    // 多个参数
                    splitParams(paramsUrl.split("&"))
                } else {
                    // 只有一个参数
                    splitParams(arrayListOf(paramsUrl))
                }
                params.putAll(map)
            }
            return params
        }


        /**
         * 分离参数
         * @param params 【a=1】
         * @return 【a:1】
         */
        private fun splitParams(params: List<String>): MutableMap<String, String> {
            val paramsMap = mutableMapOf<String, String>()
            params.forEach {
                if (it.contains("=")) {
                    val split = it.split("=")
                    paramsMap[split.first()] = split.last()
                }
            }
            return paramsMap;
        }


        /**
         * 内部跳转，打开应用内部页面
         * 内链协议:
         * inner://openex.com
         */
        private fun innerJump(routerName: String, params: MutableMap<String, String>?) {
            val route = ARouter.getInstance().build(routerName)
            params?.forEach {
                route.withString(it.key, it.value)
            }
            route.navigation()
        }


        /**
         * 外部跳转，打开外部页面（H5,浏览器,等外部页面）
         */
        private fun outerJump(context: Context, url: String) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putString("url", url)
                putExtras(bundle)
            })
        }


        private fun errorLog(jumpUrl: String) {
            "协议解析失败：jumpUrl = $jumpUrl".loge()
        }
    }

}