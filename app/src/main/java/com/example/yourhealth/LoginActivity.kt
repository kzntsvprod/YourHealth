package com.example.yourhealth

import android.content.Intent
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

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var passwordInput: EditText
    private lateinit var togglePassword: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        auth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.regButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        passwordInput = findViewById<EditText>(R.id.passInput)
        togglePassword = findViewById<ImageView>(R.id.passIcon)

        passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()

        togglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
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

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Успішний вхід! Вітаємо!",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Помилка входу: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}