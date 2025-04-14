package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class RegActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        auth = FirebaseAuth.getInstance()

        val backButton = findViewById<Button>(R.id.backButton)
        val registerButton = findViewById<Button>(R.id.regButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passInput)

        backButton.setOnClickListener {
            finish()
        }


        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.length >= 6) {
                registerUser(email, password)
            } else {
                Log.e("Register", "Помилка")
            }
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register", "Успішна реєстрація")
                    finish() // повернутись назад
                } else {
                    Log.e("Register", "Помилка: ${task.exception?.message}")
                }
            }
    }
}
