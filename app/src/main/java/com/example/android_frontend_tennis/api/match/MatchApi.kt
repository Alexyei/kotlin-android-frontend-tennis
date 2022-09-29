package com.example.android_frontend_tennis.api.match

import com.example.android_frontend_tennis.MatchCard
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MatchApi {
    @POST("insert-or-update")
    suspend fun insertOrUpdate(
        @Body request: MatchRequestInsertOrUpdate
    ):Boolean

    @GET("all-matches")
    suspend fun getAll():ArrayList<MatchResponse>

//    @POST("signin")
//    suspend fun signIn(
//        @Body request: AuthRequestLogin
//    ): TokenResponse
//
//    @GET("authenticate")
//    suspend fun authenticate(
//        @Header("Authorization") token: String
//    )
}