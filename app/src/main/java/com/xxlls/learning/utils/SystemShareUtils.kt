package com.xxlls.learning.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.xxlls.learning.R

class SystemShareUtils {

    companion object

    val INSTAGRAM_PACKAGE: String = "com.instagram.android";

    /**
     * 文本分享
     */
    fun shareText(context: Context, contentText: String, sharePackageName: String) {
        try {
            val textIntent = Intent(Intent.ACTION_SEND)
            textIntent.type = "image/*"
            textIntent.setPackage(sharePackageName)
            textIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            textIntent.putExtra(Intent.EXTRA_TEXT, Uri.parse(contentText))

            context.startActivity(Intent.createChooser(textIntent, ""))
        } catch (e: Exception) {
            Toast.makeText(context, "shareText Error", Toast.LENGTH_SHORT).show()
        }

    }

    fun shareIns(context: Context, contentText: String) {
        val backgroundAssetUri: Uri =
            Uri.Builder().scheme("res").path("${R.mipmap.ic_launcher}").build()

        val imageIntent = Intent(Intent.ACTION_SEND)
        imageIntent.type = "image/*"
        imageIntent.putExtra(Intent.EXTRA_STREAM, backgroundAssetUri)
        imageIntent.setPackage(INSTAGRAM_PACKAGE)
        if (checkAppInstalled(context, INSTAGRAM_PACKAGE)) {
            context.startActivity(Intent.createChooser(imageIntent, "share"))
        } else {
            Toast.makeText(context, "$INSTAGRAM_PACKAGE 未安装", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 根据包名判断是否安装app
     * @param context 上下文
     * @param pkgName 包名字符串
     * @return 是否安装布尔值
     */
    private fun checkAppInstalled(context: Context, pkgName: String): Boolean {
        var isInstalled = false
        if (pkgName == null || pkgName.isEmpty()) {
            isInstalled = false
        }

        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo != null) {
            isInstalled = true
        }
        return isInstalled
    }

    /**
     * 当前版本是否大于 7.0
     *
     * @return true 是
     */
    private fun checkAndroidNotBelowN(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

}