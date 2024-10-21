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
import com.example.chattoyou.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    val TAG = "MainActivityTEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        binding.buttonNextPage.setOnClickListener {
            authorization()
        }
        binding.textViewRegistation.setOnClickListener {
            val intent = RegistrationActivity.newIntent(this@LoginActivity)
            startActivity(intent)
        }
        binding.textViewForgotPassword.setOnClickListener {
            startActivity(ResetPasswordActivity.newIntent(this@LoginActivity))
        }
    }

    fun getMailAndPassword(): Boolean {
        with(binding) {
            val mail = editTextMail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            if (mail == "") {
                Toast.makeText(this@LoginActivity, "Введите почту", Toast.LENGTH_SHORT).show()
                return false
            } else if (password == "") {
                Toast.makeText(
                    this@LoginActivity,
                    "Введите пароль",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else return true
        }
    }

    fun authorization() {
        if (!getMailAndPassword()) return
        val mail = binding.editTextMail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        auth.signInWithEmailAndPassword(mail, password)
            .addOnSuccessListener {
                //Переход на некст активити
                val intent = UsersActivity.newIntent(this@LoginActivity)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Почта или пароль не найдены", Toast.LENGTH_SHORT).show()
                Log.d(TAG, it.message.toString())
            }
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
