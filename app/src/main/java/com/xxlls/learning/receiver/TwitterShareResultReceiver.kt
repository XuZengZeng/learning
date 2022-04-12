package com.xxlls.learning.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.twitter.sdk.android.tweetcomposer.TweetUploadService

class TwitterShareResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val intentExtras = intent.extras
            when {
                TweetUploadService.UPLOAD_SUCCESS == intent.action -> {
                    // success Twitter分享成功的回调
                    val tweetId = intentExtras?.getLong(TweetUploadService.EXTRA_TWEET_ID)
                    Toast.makeText(context, "Twitter Share Success $tweetId", Toast.LENGTH_SHORT)
                        .show()
                }
                TweetUploadService.UPLOAD_FAILURE == intent.action -> {
                    Toast.makeText(context, "Twitter Share Failure", Toast.LENGTH_SHORT).show()
                    // failure
                    intentExtras?.getParcelable(TweetUploadService.EXTRA_RETRY_INTENT)
                }
                TweetUploadService.TWEET_COMPOSE_CANCEL == intent.action -> {
                    // cancel
                    Toast.makeText(context, "Twitter Share Cancel", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}