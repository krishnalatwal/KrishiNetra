package com.example.krishinetra

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.krishinetra.database.AppDatabase
import com.example.krishinetra.database.ScanResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    private val GALLERY_REQUEST = 200

    private val CAMERA_REQUEST = 100

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        imageView = findViewById(R.id.imageView)

        val galleryBtn = findViewById<Button>(R.id.galleryBtn)
        val predictBtn = findViewById<Button>(R.id.predictBtn)
        val cameraBtn = findViewById<Button>(R.id.cameraBtn)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "krishinetra_db"
        ).build()

        galleryBtn.setOnClickListener {
            openGallery()
        }

        cameraBtn.setOnClickListener {
            openCamera()
        }

        predictBtn.setOnClickListener {
            imageUri?.let {
                uploadImage(it)
            } ?: Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_REQUEST) {

                imageUri = data?.data
                imageView.setImageURI(imageUri)
            }

            if (requestCode == CAMERA_REQUEST) {

                val photo = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(photo)

                val file = File(filesDir, "camera_${System.currentTimeMillis()}.jpg")
                val out = FileOutputStream(file)

                photo.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()

                imageUri = Uri.fromFile(file)
            }
        }
    }

    private fun uploadImage(uri: Uri) {

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

        val file = File(filesDir, "scan_${System.currentTimeMillis()}.jpg")
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
                        builder.append("Top Predictions\n\n")

                        for (i in predictions.indices) {

                            val p = predictions[i]
                            val conf = "%.2f".format(p.confidence)

                            builder.append("${i + 1}. ${p.disease} ($conf%)\n")
                            builder.append("Treatment: ${p.treatment}\n\n")
                        }

                        val topPrediction = predictions[0]

                        val date = SimpleDateFormat(
                            "dd MMM yyyy HH:mm",
                            Locale.getDefault()
                        ).format(Date())

                        val scanResult = ScanResult(
                            imageUri = file.absolutePath,
                            disease = topPrediction.disease,
                            confidence = topPrediction.confidence,
                            date = date
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            db.scanResultDao().insert(scanResult)
                        }

                        val intent = Intent(this@ScanActivity, ResultActivity::class.java)

                        intent.putExtra("imageUri", file.absolutePath)
                        intent.putExtra("predictions", builder.toString())

                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(
                call: retrofit2.Call<PredictionResponse>,
                t: Throwable
            ) {

                Toast.makeText(
                    this@ScanActivity,
                    "Prediction failed: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}