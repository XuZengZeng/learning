package com.xxlls.learning

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.xxlls.learning.flutter.FlutterTestActivity
import com.xxlls.learning.route.RouteConst
import com.xxlls.learning.route.RouteHelper
import com.xxlls.learning.ui_test.RecyclerViewActivity
import com.xxlls.learning.ui_test.ShareActivity


class MainActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private companion object val RC_SIGN_IN = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addEvent()
        initGoogle()
        initGoogleTwo()

    }

    private fun initGoogle() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("593652770374-ablhanv25521lv3gla27m0j6n0ut6kvn.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .build()

    }


    private fun initGoogleTwo() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } else {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, 200,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("TAG", e.localizedMessage)
                }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Log.e("TAG", account.toString())
            Toast.makeText(this,"Signed in successfully",Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this,"授权失败，请稍后再试",Toast.LENGTH_SHORT).show()
        }
    }


    private fun addEvent() {
        findViewById<View>(R.id.hello_world).setOnClickListener {
            FlutterTestActivity.start(this)
        }

        findViewById<View>(R.id.sign_in_button).setOnClickListener {

            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, 200,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("TAG", e.localizedMessage)
                }
        }

    }

    fun startAnimationActivity(view: View) {
        RouteHelper.linkJump(this, RouteConst.getInnerRouteUrl(RouteConst.APP_ANIMATION))
    }


    fun startRecyclerViewActivity(view: View) {
        RecyclerViewActivity.start(this)
    }

    /**
     * 跳转分享
     */
    fun startShareActivity(view: View) {
        ShareActivity.start(this)
    }

    fun openVerifyActivity(view: View) {
        ARouter.getInstance().build(RouteConst.APP_VERIFY).navigation()
    }

    fun googleLogin(view: View) {
        signIn()
    }


}