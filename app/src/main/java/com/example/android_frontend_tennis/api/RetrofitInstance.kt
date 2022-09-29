package com.example.android_frontend_tennis.api


import com.example.android_frontend_tennis.api.auth.AuthApi
import com.example.android_frontend_tennis.api.match.MatchApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

//    val api = Retrofit.Builder()
//        .baseUrl("https://jsonplaceholder.typicode.com")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(TodoApi::class.java)
    val authApi =
            Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

        val matchApi =
                Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MatchApi::class.java)
}