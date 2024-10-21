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
import com.example.chattoyou.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private val TAG = "RegistrationActivityTEST"
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    var mail = ""
    var password = ""
    var name = ""
    var surname = ""
    var age = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        binding.buttonNextPage.setOnClickListener {
            if (getMailAndPassword())
                loginUser(mail, password)
        }
    }
    fun registration() {
        auth.createUserWithEmailAndPassword(mail, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                user!!.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@RegistrationActivity,
                                "Письмо с подтверждением отправлено на почту",
                                Toast.LENGTH_LONG).show()
                            Log.d(TAG, "Email sent.")
                        }
                        else {
                            Toast.makeText(this@RegistrationActivity,
                                "Произошла ошибка. Повторите позже",
                                Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Error in registration().")
                            auth.signOut()
                        }
                    }
            }
    }
    private fun loginUser(email: String, password: String) {
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        startActivity(UsersActivity.newIntent(this@RegistrationActivity))
                    } else {
                        Toast.makeText(this, "Подтвердите аккаунт", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Email on loginUser73.")
                        auth.signOut() // Выход из аккаунта
                    }
                } else {
                    Log.d(TAG, "Email on loginUser78." + task.exception?.message.toString())
                }
            }
    }
    fun getMailAndPassword(): Boolean {
        with(binding) {
            mail = editTextEmail.text.toString().trim()
            password = editTextPassword.text.toString().trim()
            name = editTextName.text.toString().trim()
            surname = editTextSurname.text.toString().trim()
            age = editTextAge.text.toString().trim()
            if (mail == "") {
                Toast.makeText(this@RegistrationActivity, "Введите почту", Toast.LENGTH_SHORT).show()
                return false
            } else if (password == "") {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Введите пароль",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else if (name == "") {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Введите имя",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else if (surname == "") {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Введите фамилию",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else if (age == "") {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Введите возраст",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else {
                registration()
                return true
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    companion object {
        fun newIntent(context: Context): Intent{
            return Intent(context, RegistrationActivity::class.java)
        }
    }
}