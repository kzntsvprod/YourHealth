package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)

        auth = FirebaseAuth.getInstance()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val surnameInput = findViewById<EditText>(R.id.surnameInput)
        val thirdnameInput = findViewById<EditText>(R.id.thirdnameInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val changeButton = findViewById<Button>(R.id.changeButton)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        changeButton.setOnClickListener {

            val name = nameInput.text.toString()
            val surname = surnameInput.text.toString()
            val thirdname = thirdnameInput.text.toString()
            val weight = weightInput.text.toString().toIntOrNull()
            val height = heightInput.text.toString().toIntOrNull()

            if (weight == null || height == null) {
                Toast.makeText(this, "Будь ласка, введіть правильні числові значення для ваги та зросту", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveUserData(name, surname, thirdname, weight, height)
        }

        loadUserData()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun saveUserData(name: String, surname: String, thirdname:String, weight: Int, height: Int) {
        val userId = auth.currentUser?.uid ?: return

        val userMap = mapOf(
            "firstName" to name,
            "lastName" to surname,
            "middleName" to thirdname,
            "weight" to weight,
            "height" to height
        )

        db.collection("users").document(userId)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Дані успішно збережено", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Помилка збереження даних", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val middleName = document.getString("middleName") ?: ""
                    val weight = document.getLong("weight")?.toInt()
                    val height = document.getLong("height")?.toInt()

                    findViewById<EditText>(R.id.nameInput).setText(firstName)
                    findViewById<EditText>(R.id.surnameInput).setText(lastName)
                    findViewById<EditText>(R.id.thirdnameInput).setText(middleName)
                    findViewById<EditText>(R.id.weightInput).setText(weight?.toString() ?: "")
                    findViewById<EditText>(R.id.heightInput).setText(height?.toString() ?: "")
                } else {
                    Toast.makeText(this, "Дані користувача не знайдено", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Помилка завантаження даних: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("UserActivity", "Помилка завантаження даних", e)
            }
    }

}