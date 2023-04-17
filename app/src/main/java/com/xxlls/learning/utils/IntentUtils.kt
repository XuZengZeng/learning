package com.xxlls.learning.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager

class IntentUtils {

    companion object {

        /**
         * 获取系统设置指纹页面 Intent
         */
        fun getFingerprintIntent(): Intent {
            val enrollIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
            } else {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    component =
                        ComponentName("com.android.settings", "com.android.settings.Settings")
                }
            }
            return enrollIntent
        }
    }
}