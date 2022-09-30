package com.example.android_frontend_tennis.api.match

import com.example.android_frontend_tennis.MatchCard
import retrofit2.http.*

interface MatchApi {
    @POST("insert-or-update")
    suspend fun insertOrUpdate(
        @Body request: MatchRequestInsertOrUpdate
    ):Boolean

    @GET("all-matches")
    suspend fun getAll():ArrayList<MatchResponse>

    @GET("my-matches")
    suspend fun getMyMatches(
        @Header("Authorization") token: String
    ):ArrayList<MatchResponse>

    @DELETE("delete-match/{id}")
    suspend fun deleteMatch(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ):Boolean

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