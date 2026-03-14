package com.example.krishinetra.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_results")
data class ScanResult(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val imageUri: String,

    val disease: String,

    val confidence: Float,

    val date: String
)