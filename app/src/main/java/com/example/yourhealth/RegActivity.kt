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
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class RegActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var togglePassword: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        auth = FirebaseAuth.getInstance()

        val backButton = findViewById<Button>(R.id.backButton)
        val registerButton = findViewById<Button>(R.id.regButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        passwordInput = findViewById<EditText>(R.id.passInput)
        confirmPasswordInput = findViewById<EditText>(R.id.confirmPassInput)
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
            val confirmPassword = confirmPasswordInput.text.toString()
            val phone = phoneInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && phone.isNotEmpty()) {
                if (password.length >= 6) {
                    if (password == confirmPassword) {
                        registerUser(email, password, phone)
                    } else {
                        Toast.makeText(this, "Пароль не підтверджено", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Пароль повинен містити щонайменше 6 символів", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Усі поля мають бути заповнені.", Toast.LENGTH_SHORT).show()
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

    private fun registerUser(email: String, password: String, phone:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    if (uid != null) {
                        val db = FirebaseFirestore.getInstance()
                        val userMap = hashMapOf(
                            "email" to email,
                            "phone" to phone
                        )

                        db.collection("users").document(uid).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Реєстрація успішна!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MenuActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Помилка збереження у Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
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