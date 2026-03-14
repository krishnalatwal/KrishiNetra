package com.example.krishinetra

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val imageView = findViewById<ImageView>(R.id.resultImage)
        val predictionText = findViewById<TextView>(R.id.predictionText)

        val imageUri = intent.getStringExtra("imageUri")
        val predictions = intent.getStringExtra("predictions")

        imageView.setImageURI(Uri.parse(imageUri))
        predictionText.text = predictions
    }
}