package com.v1nic1us.music_streaming_app

import android.content.ContentProviderClient
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.v1nic1us.music_streaming_app.databinding.ActivityLoginScreenBinding
import com.v1nic1us.music_streaming_app.databinding.ActivityRegisterScreenBinding

class LoginScreen : AppCompatActivity() {

    private val REQ_ONE_TAP = 2
    private lateinit var auth: FirebaseAuth
    private var binding: ActivityLoginScreenBinding? = null
    private lateinit var  oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken

                when {
                    idToken != null -> {
                        Log.d(TAG, "Got ID Token.")
                        val firebaseAuthCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseAuthCredential).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                var intent = Intent(this@LoginScreen, MainActivity::class.java)
                                startActivity(intent)
                                Log.d(TAG, "signInWithCredential:Success")
                            } else {
                                Log.d(TAG, "signInWithCredential:Failure", task.exception)
                            }
                        }
                    } else -> {
                        Log.d(TAG, "No id token!")
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginScreenBinding.inflate((layoutInflater))
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()

                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("1009204268890-cjob68pdoiv8p3e8kp1pkrl90ot8rmi0.apps.googleusercontent.com")
                    .build()).setAutoSelectEnabled(true).build()
        btnLogin()
        btnCreateAccount()
        btnGoogleLogin()
    }

    private fun signInWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.d(TAG, "signInUserWithEmailAndPassword:Success")
                val user = auth.currentUser
                var intent = Intent(this@LoginScreen, MainActivity::class.java)
                startActivity(intent)
            }else {
                Log.w(TAG, "signInUserWithEmailAndPassword:Failure", task.exception)
                Toast.makeText(baseContext, "Email ou senha incorreto!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnLogin(){
        binding?.btLogin?.setOnClickListener{
            var email: String = binding?.etEmail?.text.toString()
            var password: String = binding?.etPassword?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                signInWithEmailAndPassword(email, password)
            }else {
                Toast.makeText(this@LoginScreen, "Por favor preencha os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnGoogleLogin(){
        binding?.rlLoginGoogle?.setOnClickListener{
            showAuthGoogle()
        }
    }

    private fun showAuthGoogle(){
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            try {
                startIntentSenderForResult(result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0, null)
            }catch (e: IntentSender.SendIntentException){
                Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }.addOnFailureListener(this) { e ->
            if(e.localizedMessage.contains("Cannot find a matching credential")){
                Toast.makeText(this@LoginScreen, "Não existi conta google logadas", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, e.localizedMessage)
        }
    }

    private fun btnCreateAccount(){
        binding?.llSignUp?.setOnClickListener{
            var intent = Intent(this@LoginScreen, RegisterScreen::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private var TAG = "EmailAndPassword"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}