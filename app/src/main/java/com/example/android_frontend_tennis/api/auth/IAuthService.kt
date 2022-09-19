package com.example.android_frontend_tennis.api.auth

import android.content.SharedPreferences

interface IAuthService {
    suspend fun signUp(username: String, password: String, repeat:String): AuthResult<Unit>
    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}