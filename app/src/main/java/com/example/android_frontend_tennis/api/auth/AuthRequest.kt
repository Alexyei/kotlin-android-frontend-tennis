package com.example.android_frontend_tennis.api.auth

data class AuthRequestLogin(
    val username: String,
    val password: String,
)

data class AuthRequestSignup(
    val username: String,
    val password: String,
    val repeat:String
)