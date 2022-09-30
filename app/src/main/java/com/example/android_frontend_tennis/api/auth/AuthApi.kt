package com.example.android_frontend_tennis.api.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequestSignup
    )

    @POST("signin")
    suspend fun signIn(
        @Body request: AuthRequestLogin

    ): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}