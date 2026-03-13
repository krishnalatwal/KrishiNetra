package com.example.krishinetra

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val CAMERA_REQUEST = 100
    private val GALLERY_REQUEST = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        val cameraBtn = findViewById<Button>(R.id.cameraBtn)
        val galleryBtn = findViewById<Button>(R.id.galleryBtn)

        cameraBtn.setOnClickListener {
            openCamera()
        }

        galleryBtn.setOnClickListener {
            openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                val photo = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(photo)
            }

            if (requestCode == GALLERY_REQUEST) {
                val imageUri = data?.data
                imageView.setImageURI(imageUri)
            }
        }
    }
}