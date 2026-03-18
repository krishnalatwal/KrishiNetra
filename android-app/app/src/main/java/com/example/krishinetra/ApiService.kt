package com.example.krishinetra

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Query("lang") lang: String
    ): Call<PredictionResponse>
}