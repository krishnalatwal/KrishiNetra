package com.example.krishinetra

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val scanCard = findViewById<MaterialCardView>(R.id.scanCard)
        val historyCard = findViewById<MaterialCardView>(R.id.historyCard)

        scanCard.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        historyCard.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
