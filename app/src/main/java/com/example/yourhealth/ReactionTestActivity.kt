package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.graphics.Color
import android.widget.Toast
import kotlin.random.Random

class ReactionTestActivity : AppCompatActivity() {

    private lateinit var reactionButton: Button
    private lateinit var timerLabel: TextView
    private lateinit var reactionInput: TextView

    private var startTime: Long = 0L
    private var canClick = false
    private var handler = Handler(Looper.getMainLooper())
    private var delayRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reaction_activity)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val profileText = findViewById<TextView>(R.id.profileText)

        timerLabel = findViewById(R.id.timerLabel)
        reactionInput = findViewById(R.id.reactionInput)
        reactionButton = findViewById(R.id.reactionButton)

        val goToUserActivity = View.OnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        reactionButton.setOnClickListener {
            if (canClick) {
                recordReaction()
            } else {
                val currentColor = reactionButton.backgroundTintList?.defaultColor
                if (currentColor == Color.GRAY) {
                    Toast.makeText(this, "Зачекай!", Toast.LENGTH_SHORT).show()
                } else {
                    startTest()
                }
            }
        }

        profileIcon.setOnClickListener(goToUserActivity)
        profileText.setOnClickListener(goToUserActivity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    private fun startTest() {
        canClick = false
        reactionInput.text = ""
        reactionButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
        timerLabel.text = "Готуйтесь..."

        val delay = Random.nextLong(2000, 5000)

        delayRunnable = Runnable {
            reactionButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
            reactionButton.text = "Стоп"
            timerLabel.text = "Натискай!"
            startTime = SystemClock.elapsedRealtime()
            canClick = true

            val updateTimerRunnable = object : Runnable {
                override fun run() {
                    if (canClick) {
                        val elapsedTime = SystemClock.elapsedRealtime() - startTime
                        timerLabel.text = "Час: ${elapsedTime} мс"
                        handler.postDelayed(this, 50)
                    }
                }
            }
            handler.post(updateTimerRunnable)
        }

        handler.postDelayed(delayRunnable!!, delay)
    }

    private fun recordReaction() {
        val reactionTime = SystemClock.elapsedRealtime() - startTime
        timerLabel.text = "Час реакції:"
        reactionInput.text = "$reactionTime мс"
        reactionButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
        reactionButton.text = "Старт"
        canClick = false
        handler.removeCallbacks(delayRunnable!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(delayRunnable ?: Runnable { })
    }
}