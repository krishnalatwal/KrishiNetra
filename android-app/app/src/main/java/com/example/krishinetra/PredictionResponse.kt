package com.example.krishinetra

data class PredictionResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val disease: String,
    val confidence: Float,
    val treatment: String
)