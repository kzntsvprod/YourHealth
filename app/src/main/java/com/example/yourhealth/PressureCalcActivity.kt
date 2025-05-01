package com.example.yourhealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PressureCalcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressure_activity)

        val higherPressureInput = findViewById<EditText>(R.id.higherPressureInput)
        val lowerPressureInput = findViewById<EditText>(R.id.lowerPressureInput)
        val resultOutput = findViewById<EditText>(R.id.pressureInput)
        val adviceOutput = findViewById<EditText>(R.id.adviceInput)
        val calcButton = findViewById<Button>(R.id.calcButton)
        val backButton = findViewById<Button>(R.id.backButton)

        calcButton.setOnClickListener {
            try {
                val higher = higherPressureInput.text.toString().toInt()
                val lower = lowerPressureInput.text.toString().toInt()
                val (result, advice) = analyzePressure(higher, lower)
                resultOutput.setText(result)
                adviceOutput.setText(advice)
            } catch (e: NumberFormatException) {
                resultOutput.setText("Введіть коректні числа")
                adviceOutput.setText("")
            }
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

    private fun analyzePressure(higher: Int, lower: Int): Pair<String, String> {
        return when {
            higher < 90 || lower < 60 -> Pair(
                "Гіпотонія (низький тиск)",
                "Рекомендації:\n" +
                        "- Збільште споживання рідини\n" +
                        "- Додайте трохи солі до раціону\n" +
                        "- Регулярні легкі фізичні навантаження\n" +
                        "- Консультація з лікарем при постійних симптомах"
            )
            higher in 90..119 && lower in 60..79 -> Pair(
                "Ідеальний тиск",
                "Вітаємо! Ваш тиск в ідеальних межах.\n" +
                        "Поради:\n" +
                        "- Продовжуйте вести здоровий спосіб життя\n" +
                        "- Регулярно контролюйте тиск\n" +
                        "- Уникайте стресу"
            )
            higher in 120..129 && lower < 80 -> Pair(
                "Нормальний тиск",
                "Ваш тиск у межах норми.\n" +
                        "Поради:\n" +
                        "- Продовжуйте дотримуватися здорового способу життя\n" +
                        "- Уникайте надмірного споживання солі\n" +
                        "- Регулярні фізичні навантаження"
            )
            higher in 130..139 || lower in 80..89 -> Pair(
                "Підвищений нормальний тиск",
                "Увага! Ризик розвитку гіпертонії.\n" +
                        "Рекомендації:\n" +
                        "- Зменшіть споживання солі\n" +
                        "- Контролюйте вагу\n" +
                        "- Регулярні фізичні вправи\n" +
                        "- Обмежте алкоголь\n" +
                        "- Консультація з лікарем"
            )
            higher in 140..159 || lower in 90..99 -> Pair(
                "Гіпертонія 1 ступеня",
                "Необхідні заходи:\n" +
                        "- Термінова консультація з лікарем\n" +
                        "- Зміна способу життя\n" +
                        "- Регулярний моніторинг тиску\n" +
                        "- Зниження споживання солі та алкоголю\n" +
                        "- Контроль ваги"
            )
            higher in 160..179 || lower in 100..109 -> Pair(
                "Гіпертонія 2 ступеня",
                "Негайні дії:\n" +
                        "- Терміново зверніться до лікаря\n" +
                        "- Можлива необхідність медикаментозного лікування\n" +
                        "- Суворе дотримання здорового способу життя\n" +
                        "- Регулярний контроль тиску"
            )
            higher >= 180 || lower >= 110 -> Pair(
                "Гіпертонічна криза",
                "НЕГАЙНА ДОПОМОГА:\n" +
                        "- Виклик швидкої допомоги\n" +
                        "- Прийняття ліків за призначенням лікаря\n" +
                        "- Спокій у горизонтальному положенні\n" +
                        "- Вимірювання тиску кожні 15 хвилин"
            )
            else -> Pair(
                "Невідомий стан",
                "Перевірте правильність введених даних"
            )
        }
    }
}