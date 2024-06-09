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
import com.v1nic1us.music_streaming_app.databinding.ActivityRegisterScreenBinding

class RegisterScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var binding: ActivityRegisterScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterScreenBinding.inflate((layoutInflater))
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding?.btRegister?.setOnClickListener{
            var email: String = binding?.etEmail?.text.toString()
            var password: String = binding?.etPassword?.text.toString()
            var confirmPassword: String = binding?.etConfirmPassword?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password == confirmPassword){
                    createUserWithEmailAndPassword(email, password)
                }else {
                    Toast.makeText(this@RegisterScreen, "Senhas incompativel!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else {
                Toast.makeText(this@RegisterScreen, "Por favor preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d(TAG, "createUserWithEmailAndPassword:Success")
                val user = auth.currentUser
                var intent = Intent(this@RegisterScreen, MainActivity::class.java)
                startActivity(intent)
            }else {
                Log.w(TAG, "createUserWithEmailAndPassword:Failure", task.exception)
                Toast.makeText(baseContext, "Error ao criar conta!", Toast.LENGTH_SHORT).show()
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