package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.auth.FirebaseAuth
import android.widget.ArrayAdapter

class CaloriesCalcActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calories_activity)

        auth = FirebaseAuth.getInstance()

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val profileText = findViewById<TextView>(R.id.profileText)

        val radioMale = findViewById<RadioButton>(R.id.radioMale)
        val radioFemale = findViewById<RadioButton>(R.id.radioFemale)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val activitySpinner = findViewById<Spinner>(R.id.activitySpinner)

        val activityLevels = resources.getStringArray(R.array.activity_levels)

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            activityLevels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = adapter


        val calculateButton = findViewById<Button>(R.id.calcButton)
        val resultText = findViewById<TextView>(R.id.caloriesCalcInput)


        val goToUserActivity = View.OnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener(goToUserActivity)
        profileText.setOnClickListener(goToUserActivity)

        loadUserData()

        calculateButton.setOnClickListener {
            val ageStr = ageInput.text.toString()
            val weightStr = weightInput.text.toString()
            val heightStr = heightInput.text.toString()
            val activityLevel = activitySpinner.selectedItemPosition

            if (ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageStr.toInt()
            val weight = weightStr.toDouble()
            val height = heightStr.toDouble()

            val bmr = if (radioMale.isChecked) {
                88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age)
            } else {
                447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age)
            }

            val multiplier = when (activityLevel) {
                0 -> 1.2
                1 -> 1.375
                2 -> 1.55
                3 -> 1.725
                4 -> 1.9
                else -> 1.2
            }

            val result = bmr * multiplier
            resultText.text = "Ваш денний рівень калорій: %.2f. Для схуднення: %.2f. Для набору ваги: %.2f".format(result, result - 500, result + 300)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val weight = document.getLong("weight")?.toInt()
                    val height = document.getLong("height")?.toInt()

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