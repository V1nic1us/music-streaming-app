package com.v1nic1us.music_streaming_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.v1nic1us.music_streaming_app.databinding.ActivityLoginScreenBinding
import com.v1nic1us.music_streaming_app.databinding.ActivityRegisterScreenBinding

class LoginScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var binding: ActivityLoginScreenBinding? = null
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

        binding?.btLogin?.setOnClickListener{
            var email: String = binding?.etEmail?.text.toString()
            var password: String = binding?.etPassword?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                signInWithEmailAndPassword(email, password)
            }else {
                Toast.makeText(this@LoginScreen, "Por favor preencha os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.llSignUp?.setOnClickListener{
            var intent = Intent(this@LoginScreen, RegisterScreen::class.java)
            startActivity(intent)
        }
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

    companion object {
        private var TAG = "EmailAndPassword"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}