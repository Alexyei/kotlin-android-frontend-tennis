package com.example.android_frontend_tennis.api.match

import android.content.SharedPreferences
import com.example.android_frontend_tennis.MatchCard

interface IMatchService {
    suspend fun insertOrUpdate(match:MatchCard): MatchResult<Any>
    suspend fun getAll(): MatchResult<Any>
}