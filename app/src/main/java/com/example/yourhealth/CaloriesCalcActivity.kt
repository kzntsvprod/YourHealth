package com.example.yourhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.content.Intent

class CaloriesCalcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calories_activity)

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

        profileIcon.setOnClickListener(goToUserActivity)
        profileText.setOnClickListener(goToUserActivity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }
}