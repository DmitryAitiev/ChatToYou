package com.example.chattoyou

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chattoyou.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "ResetPasswordActivityTEST"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        binding.buttonReset.setOnClickListener {
            resetPassword()
        }
    }
    fun resetPassword() {
        val mail = binding.editTextEmail.text.trim().toString()
        if (!isEmailValid(mail)){
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(mail)
            .addOnSuccessListener { Toast.makeText(
                this@ResetPasswordActivity,
                "Письмо для сброса отправлено",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(LoginActivity.newIntent(this@ResetPasswordActivity))}
            .addOnFailureListener {
                Log.d(TAG, it.message.toString())
            }
    }
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ResetPasswordActivity::class.java)
        }
    }
}