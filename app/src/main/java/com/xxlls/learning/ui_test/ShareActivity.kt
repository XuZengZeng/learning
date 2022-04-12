package com.xxlls.learning.ui_test

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import com.xxlls.learning.R
import com.xxlls.learning.utils.SystemShareUtils
import java.net.URL


class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
    }


    /**
     * Twitter 分享
     */
    fun twitterShare(view: View) {
        shareToTwitter()
    }

    /**
     * Ins 分享
     */
    fun instagramShare(view: View) {
        SystemShareUtils().shareIns(
            this,
            "android.resource://" + applicationContext.packageName + "/" + R.mipmap.ic_launcher_round,
        )
    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ShareActivity::class.java)
            context.startActivity(starter)
        }
    }

    fun shareText(view: View) {
        SystemShareUtils().shareText(this, "这是分享的内容", "com.instagram.android")
    }


    /**
     * 分享到twitter
     * 若未安装twitter客户端，则会跳转到浏览器
     */
    fun shareToTwitter() {
        //这里分享一个链接，更多分享配置参考官方介绍：https://dev.twitter.com/twitterkit/android/compose-tweets
        try {
            val builder = TweetComposer.Builder(this)
                .url(URL("https://www.google.com/"))
                .image(Uri.Builder().scheme("res").path("${R.mipmap.ic_launcher}").build())
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}