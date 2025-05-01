package com.example.yourhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.View

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Переход до UserActivity при натисканні на іконку або текст
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val profileText = findViewById<TextView>(R.id.profileText)

        val goToUserActivity = View.OnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        val calcHeartRateButton = findViewById<Button>(R.id.calcHeartRateButton)

        val goToHeartRateCalcActivity = View.OnClickListener {
            val intent = Intent(this, HeartRateCalcActivity::class.java)
            startActivity(intent)
        }

        val pressureButton = findViewById<Button>(R.id.pressureButton)

        val goToPressureCalcActivity = View.OnClickListener {
            val intent = Intent(this, PressureCalcActivity::class.java)
            startActivity(intent)
        }

        val calcBmrButton = findViewById<Button>(R.id.calcBmrButton)

        val goToBmrCalcActivity = View.OnClickListener {
            val intent = Intent(this, BmrCalcActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener(goToUserActivity)
        profileText.setOnClickListener(goToUserActivity)
        calcHeartRateButton.setOnClickListener(goToHeartRateCalcActivity)
        pressureButton.setOnClickListener(goToPressureCalcActivity)
        calcBmrButton.setOnClickListener(goToBmrCalcActivity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }
}