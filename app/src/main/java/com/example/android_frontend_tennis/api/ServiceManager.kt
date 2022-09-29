package com.example.android_frontend_tennis.api

import android.content.SharedPreferences
import com.example.android_frontend_tennis.api.auth.AuthService
import com.example.android_frontend_tennis.api.match.MatchService

object ServiceManager {
    fun getAuthService(pref: SharedPreferences):AuthService{
        return AuthService(pref)
    }
    fun getMatchService():MatchService{
        return MatchService()
    }
}