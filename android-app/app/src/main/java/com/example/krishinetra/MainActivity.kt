package com.example.krishinetra

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var resultText: TextView
    private var imageUri: Uri? = null

    private val GALLERY_REQUEST = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        resultText = findViewById(R.id.resultText)

        val galleryBtn = findViewById<Button>(R.id.galleryBtn)
        val predictBtn = findViewById<Button>(R.id.predictBtn)

        galleryBtn.setOnClickListener {
            openGallery()
        }

        predictBtn.setOnClickListener {
            imageUri?.let {
                uploadImage(it)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage(uri: Uri) {

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

        val file = File(cacheDir, "upload.jpg")
        val output = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        output.flush()
        output.close()

        val requestFile = RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            file
        )

        val body = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )

        val call = RetrofitClient.instance.uploadImage(body)

        call.enqueue(object : retrofit2.Callback<PredictionResponse> {

            override fun onResponse(
                call: retrofit2.Call<PredictionResponse>,
                response: retrofit2.Response<PredictionResponse>
            ) {
                if (response.isSuccessful) {

                    val predictions = response.body()?.predictions

                    if (!predictions.isNullOrEmpty()) {

                        val builder = StringBuilder()

                        builder.append("Top Predictions:\n\n")

                        for (i in predictions.indices) {

                            val disease = predictions[i].disease
                            val confidence = "%.2f".format(predictions[i].confidence * 100)

                            builder.append("${i + 1}. $disease ($confidence%)\n")
                        }

                        resultText.text = builder.toString()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<PredictionResponse>, t: Throwable) {

                resultText.text = "Error: ${t.message}"
            }
        })
    }
}