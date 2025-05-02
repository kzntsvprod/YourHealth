package com.example.yourhealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

class BmrCalcActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bmr_activity)

        auth = FirebaseAuth.getInstance()

        val heightInput = findViewById<EditText>(R.id.heightInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val bmrResultBox = findViewById<TextView>(R.id.bmrResultBox)
        val adviceInput = findViewById<EditText>(R.id.adviceInput)
        val calcButton = findViewById<Button>(R.id.calcButton)
        val backButton = findViewById<Button>(R.id.backButton)

        loadUserData(heightInput, weightInput)

        calcButton.setOnClickListener {
            calculateBmi(heightInput, weightInput, bmrResultBox, adviceInput)
        }

        backButton.setOnClickListener {
            finish()
        }

        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val profileText = findViewById<TextView>(R.id.profileText)

        profileIcon.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        profileText.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun loadUserData(heightInput: EditText, weightInput: EditText) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val weight = document.getLong("weight")?.toInt()
                    val height = document.getLong("height")?.toInt()

                    weightInput.setText(weight?.toString() ?: "")
                    heightInput.setText(height?.toString() ?: "")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Помилка завантаження даних", Toast.LENGTH_SHORT).show()
            }
    }

    private fun calculateBmi(
        heightInput: EditText,
        weightInput: EditText,
        bmrResultBox: TextView,
        adviceInput: EditText
    ) {
        try {
            val height = heightInput.text.toString().toInt()
            val weight = weightInput.text.toString().toInt()

            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "Введіть коректні значення", Toast.LENGTH_SHORT).show()
                return
            }

            val bmi = calculateBmiValue(weight, height)
            val (result, advice) = analyzeBmi(bmi)

            bmrResultBox.text = String.format("%.1f", bmi)
            adviceInput.setText(advice)
            bmrResultBox.setTextColor(getColorForBmi(bmi))

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Введіть коректні числа", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateBmiValue(weight: Int, height: Int): Double {
        val heightInMeters = height.toDouble() / 100
        return weight.toDouble() / (heightInMeters * heightInMeters)
    }

    private fun analyzeBmi(bmi: Double): Pair<String, String> {
        return when {
            bmi < 16 -> Pair(
                "Різкий дефіцит маси",
                "Небезпечно низька вага. Необхідна негайна консультація лікаря та харчового терапевта."
            )
            bmi in 16.0..18.49 -> Pair(
                "Дефіцит маси",
                "Недостатня вага. Рекомендується:\n- Збалансоване харчування\n- Поступове збільшення калорій\n- Консультація лікаря"
            )
            bmi in 18.5..24.99 -> Pair(
                "Норма",
                "Ідеальна вага. Продовжуйте:\n- Здорове харчування\n- Регулярні фізичні навантаження\n- Контроль ваги"
            )
            bmi in 25.0..29.99 -> Pair(
                "Надмірна вага",
                "Передожиріння. Рекомендації:\n- Зменшення калорій\n- Збільшення фізактивності\n- Контроль харчування"
            )
            bmi in 30.0..34.99 -> Pair(
                "Ожиріння 1 ступеня",
                "Необхідно:\n- Консультація лікаря\n- Дієта під наглядом фахівця\n- Регулярні тренування"
            )
            bmi in 35.0..39.99 -> Pair(
                "Ожиріння 2 ступеня",
                "Серйозна проблема. Дії:\n- Термінова консультація лікаря\n- Комплексне лікування\n- Сувора дієта"
            )
            bmi >= 40 -> Pair(
                "Ожиріння 3 ступеня",
                "Небезпечний стан. НЕГАЙНО:\n- Медичне обстеження\n- Спеціалізоване лікування\n- Постійний нагляд"
            )
            else -> Pair(
                "Помилка",
                "Неможливо визначити стан. Перевірте введені дані."
            )
        }
    }

    private fun getColorForBmi(bmi: Double): Int {
        return when {
            bmi < 18.5 -> resources.getColor(R.color.underweight_color)
            bmi in 18.5..24.99 -> resources.getColor(R.color.normal_weight_color)
            bmi in 25.0..29.99 -> resources.getColor(R.color.overweight_color)
            else -> resources.getColor(R.color.obesity_color)
        }
    }
}