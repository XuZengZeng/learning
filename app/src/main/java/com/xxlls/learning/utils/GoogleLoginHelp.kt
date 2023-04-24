package com.xxlls.learning.utils

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * implementation 'com.google.android.gms:play-services-auth:20.5.0'
 * 用戶Google 三分認證登錄的交互幫助類,統一構造參數,統一處理請求結果
 */
class GoogleLoginHelp {

    public lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private var clientID: String? = null

    public companion object {
        // Google 一键式三方登录
        val RC_SIGN_IN = 200

        // 旧版Google三放登录
        val RC_SIGN_IN_PRO = 300
    }

    public fun initGoogle(serverClientID: String, activity: Activity) {
        clientID = serverClientID
        oneTapClient = Identity.getSignInClient(activity)
        signInRequest = BeginSignInRequest.builder().setPasswordRequestOptions(
            BeginSignInRequest
                .PasswordRequestOptions
                .builder()
                .setSupported(true)
                .build()
        ).setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                .GoogleIdTokenRequestOptions
                .builder().setSupported(true)
                .setServerClientId(serverClientID)
                .setFilterByAuthorizedAccounts(true)
                .build()
        ).setAutoSelectEnabled(true).build()

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(serverClientID)
            .requestIdToken(serverClientID).build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }


    /**
     * 開始Google 登錄, 如果有賬號信息使用Google 最新版一鍵登錄,否則使用老版本登錄方式
     * @param activity 對應Activity 數據結果將會掉到 對應Activity的 onActivityForResult() 方法中,通過 @onGoogleSignInResult() 中注冊回回掉同意接受結果
     */
    fun signIn(activity: Activity) {
        if (android.text.TextUtils.isEmpty(clientID)) {
            Toast.makeText(activity,"授權失敗，请稍后再试",Toast.LENGTH_SHORT).show()
            Log.e("GoogleLoginHelp", "clientID Not init ........")
            return
        }
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account == null) {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            activity.startActivityForResult(signInIntent, RC_SIGN_IN_PRO)
        } else {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(activity) { result ->
                    try {
                        activity.startIntentSenderForResult(
                            result.pendingIntent.intentSender, RC_SIGN_IN,
                            null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Toast.makeText(activity,"授權失敗，请稍后再试",Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener(activity) { e ->
                    Toast.makeText(activity,"授權失敗，请稍后再试",Toast.LENGTH_SHORT).show()
                }
        }
    }


    /**
     * 在調用 signIn 的 onActivityForResult中的調用此方法,接受Google 返回數據,用於統一結果處理
     */
    fun onGoogleSignInResult(
        requestCode: Int, data: Intent?, call: OnGoogleSignInRequestImpl?
    ) {
        if (requestCode == RC_SIGN_IN) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                call?.succeed(credential.googleIdToken)
            } catch (e: ApiException) {
                call?.error()
            }

        } else if (requestCode == RC_SIGN_IN_PRO) {
            try {
                val completedTask: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = completedTask.getResult(ApiException::class.java)
                Log.e("TAG", account.toString())
                call?.succeed(account.idToken)
            } catch (e: ApiException) {
                call?.error()
            }
        }
    }

    /**
     * Google 驗證統一邏輯處理
     */
    interface OnGoogleSignInRequestImpl {
        fun succeed(idToken: String?)
        fun error()
    }

}