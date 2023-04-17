package com.xxlls.learning.verify

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.alibaba.android.arouter.facade.annotation.Route
import com.xxlls.learning.R
import com.xxlls.learning.route.RouteConst
import com.xxlls.learning.utils.BiometricUtils
import com.xxlls.learning.utils.IntentUtils
import java.util.concurrent.Executor


@Route(path = RouteConst.APP_VERIFY)
class VerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        BiometricUtils.instance.initBiometric(this)

        verify()

        addEvent()
    }

    private fun addEvent() {
        findViewById<Button>(R.id.finger).setOnClickListener {

        }

        findViewById<Button>(R.id.face).setOnClickListener {

        }
    }

    private fun verify() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // 设备支持生物识别
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                Toast.makeText(this, "应用可以进行生物识别技术进行身份验证", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // 设备不支持生物识别
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                Toast.makeText(this, "该设备上没有搭载可用的生物特征功能", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // 设备生物识别当前不可用
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                Toast.makeText(this, "生物识别功能当前不可用", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                Toast.makeText(this, "用户没有录入生物识别数据", Toast.LENGTH_SHORT).show()

//                // 引导用户录入生物识别数据（指纹-> 人脸 -> 虹膜）
//                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                    putExtra(
//                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
//                    )
//                }
//                startActivityForResult(enrollIntent, 100)
            }
        }

//        when (biometricManager.canAuthenticate(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> {
//                // 设备支持生物识别
//                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
//            }
//
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
//                // 设备不支持生物识别
//                Log.e("MY_APP_TAG", "No biometric features available on this device.")
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
//                // 设备生物识别当前不可用
//                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                // Prompts the user to create credentials that your app accepts.
//                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                    putExtra(
//                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
//                    )
//                }
//                startActivityForResult(enrollIntent, 100)
//            }
//        }
    }


    /**
     * 开启安全验证
     */
    fun openFingerprintVerify(activity: Activity) {
        // 支持指纹但未录入,跳转系统指纹录入页面
        when (BiometricUtils.instance.getForFingerprintPreAuthenticationStatus()) {
            BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                // 引导用户录入生物识别数据（指纹-> 人脸 -> 虹膜）
                val enrollIntent = IntentUtils.getFingerprintIntent()
                activity.startActivityForResult(enrollIntent, 100)
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // 开启指纹识别
                BiometricUtils.instance.showVerifyBiometricDialog(
                    this,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int, errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(
                                applicationContext,
                                "Authentication error: $errString",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)
                            Toast.makeText(
                                applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(
                                applicationContext, "Authentication failed", Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            else -> {
                // 设备不支持指纹
                Toast.makeText(
                    applicationContext, "设备不支持指纹", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}