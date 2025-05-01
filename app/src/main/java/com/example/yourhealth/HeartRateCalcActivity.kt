package com.example.yourhealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HeartRateCalcActivity : AppCompatActivity() {
    private val ageGroups = listOf(
        AgeGroup(18, 25, listOf(40, 51, 57, 67, 78)),
        AgeGroup(26, 35, listOf(41, 52, 58, 70, 81)),
        AgeGroup(36, 45, listOf(45, 53, 59, 72, 83)),
        AgeGroup(46, 55, listOf(46, 55, 61, 74, 85)),
        AgeGroup(56, 65, listOf(48, 56, 62, 75, 86)),
        AgeGroup(66, 150, listOf(50, 57, 63, 76, 87)) // 65+ лет
    )

    private val stateMap = mapOf(
        0 to "Атлет",
        1 to "Гарна форма",
        2 to "Середня форма",
        3 to "Нижче середнього",
        4 to "Погана форма"
    )

    data class AgeGroup(
        val minAge: Int,
        val maxAge: Int,
        val pulseThresholds: List<Int>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.heartrate_activity)

        val ageInput = findViewById<EditText>(R.id.ageInput)
        val pulseInput = findViewById<EditText>(R.id.pulseInput)
        val resultOutput = findViewById<EditText>(R.id.pulseInput2)
        val adviceOutput = findViewById<EditText>(R.id.adviceInput)
        val calcButton = findViewById<Button>(R.id.calcButton)
        val backButton = findViewById<Button>(R.id.backButton)

        calcButton.setOnClickListener {
            try {
                val userAge = ageInput.text.toString().toInt()
                val userPulse = pulseInput.text.toString().toInt()
                val (result, advice) = checkPulse(userAge, userPulse)
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

    private fun checkPulse(userAge: Int, userPulse: Int): Pair<String, String> {
        val ageGroup = ageGroups.find { group ->
            userAge in group.minAge..group.maxAge
        } ?: return Pair("Невідомий віковий діапазон", "Перевірте правильність введеного віку")

        val thresholds = ageGroup.pulseThresholds
        val stateIndex = thresholds.indexOfLast { threshold -> userPulse >= threshold }

        val (result, advice) = when(stateIndex) {
            0 -> Pair(
                stateMap[0] ?: "",
                "Вітаємо! Ваш пульс свідчить про відмінну фізичну форму, характерну для професійних спортсменів. " +
                        "Продовжуйте регулярні тренування, але не забувайте про відпочинок та відновлення."
            )
            1 -> Pair(
                stateMap[1] ?: "",
                "Чудовий результат! Ваш пульс свідчить про гарну фізичну форму. " +
                        "Для підтримки такого рівня рекомендується: " +
                        "\n- 3-4 тренування на тиждень" +
                        "\n- Різноманітні фізичні навантаження" +
                        "\n- Збалансоване харчування"
            )
            2 -> Pair(
                stateMap[2] ?: "",
                "Ваш пульс у межах середньої норми. Є простір для покращення: " +
                        "\n- Збільшіть кількість тренувань до 3-4 на тиждень" +
                        "\n- Додайте кардіо-навантаження" +
                        "\n- Контролюйте режим сну та харчування" +
                        "\n- Уникайте стресу"
            )
            3 -> Pair(
                stateMap[3] ?: "",
                "Ваш пульс нижчий за середній показник. Рекомендації: " +
                        "\n- Починайте з 2-3 тренувань на тиждень" +
                        "\n- Поступово збільшуйте навантаження" +
                        "\n- Додайте ходьбу пішки щодня" +
                        "\n- Консультація з лікарем перед початком тренувань" +
                        "\n- Відмовтеся від шкідливих звичок"
            )
            4 -> Pair(
                stateMap[4] ?: "",
                "Увага! Ваш пульс свідчить про погану фізичну форму. Необхідно: " +
                        "\n- Обов'язкова консультація з лікарем" +
                        "\n- Почати з легких фізичних вправ" +
                        "\n- Щоденні прогулянки на свіжому повітрі" +
                        "\n- Повний відпочинок та здорове харчування" +
                        "\n- Уникайте стресових ситуацій" +
                        "\n- Відмова від шкідливих звичок"
            )
            else -> Pair(
                "пульс занадто низький",
                "Ваш пульс нижчий за мінімальні показники для вашого віку. " + "Рекомендується негайна консультація з лікарем."
            )
        }

        return Pair(result ?: "невідомий стан", advice)
    }
}