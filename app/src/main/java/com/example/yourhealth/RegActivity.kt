package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class RegActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var passwordInput: EditText
    private lateinit var togglePassword: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        auth = FirebaseAuth.getInstance()

        val backButton = findViewById<Button>(R.id.backButton)
        val registerButton = findViewById<Button>(R.id.regButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        passwordInput = findViewById<EditText>(R.id.passInput)
        togglePassword = findViewById<ImageView>(R.id.passIcon)

        passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()

        togglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        backButton.setOnClickListener {
            finish()
        }

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.length >= 6) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Пароль повинен містити щонайменше 6 символів", Toast.LENGTH_SHORT).show()
            }
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePassword.setImageResource(R.drawable.eye_off)
        } else {
            passwordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
            togglePassword.setImageResource(R.drawable.eye)
        }
        isPasswordVisible = !isPasswordVisible
        passwordInput.setSelection(passwordInput.text.length)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Реєстрація успішна!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Помилка реєстрації: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Register", "Помилка: ${task.exception?.message}")
                }
            }
    }
}