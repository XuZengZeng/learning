package com.xxlls.learning.utils

import android.content.Context
import androidx.annotation.NonNull
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.FragmentActivity
import com.xxlls.learning.R


class BiometricUtils {

    private var fingerprintManager: FingerprintManagerCompat? = null

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            BiometricUtils()
        }
    }

    fun initBiometric(context: Context) {
        fingerprintManager = FingerprintManagerCompat.from(context)
    }


    /**
     * 检测用户当前指纹注册状态
     */
    fun getForFingerprintPreAuthenticationStatus(): Int {
        fingerprintManager?.let {
            if (!it.isHardwareDetected) {
                // 设备没有所需的身份验证硬件
                return BiometricPrompt.ERROR_HW_NOT_PRESENT
            } else if (!it.hasEnrolledFingerprints()) {
                // 用户没有注册任何生物特征。
                return BiometricPrompt.ERROR_NO_BIOMETRICS
            } else {
                // 设备支持指纹并且存有指纹
                return BiometricManager.BIOMETRIC_SUCCESS
            }
        }
        return BiometricPrompt.ERROR_HW_NOT_PRESENT
    }

    /**
     * 通过 BiometricManager 获取指纹类型，不需要 initBiometric 操作
     */
    fun checkForFingerprintPreAuthenticationErrors(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate()
    }


    /**
     * 展示生物验证的Dialog弹窗
     * @param context 上下文
     * @param callback 验证回调
     */
    fun showVerifyBiometricDialog(context: FragmentActivity, @NonNull callback: BiometricPrompt.AuthenticationCallback) {
        val executor = ContextCompat.getMainExecutor(context)
        BiometricPrompt.ERROR_NO_BIOMETRICS
        val biometricPrompt =
            BiometricPrompt(context, executor, callback)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(" ")
            .setSubtitle(" ")
            .setNegativeButtonText(context.getString(R.string.cancel))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * 硬件是否支持指纹，且指纹可用
     * @return true:支持指纹，且指纹可用
     */
    fun isSystemFingerprint() : Boolean {
        val action = getForFingerprintPreAuthenticationStatus()
        return action == BiometricPrompt.ERROR_NO_BIOMETRICS || action == BiometricManager.BIOMETRIC_SUCCESS
    }



}