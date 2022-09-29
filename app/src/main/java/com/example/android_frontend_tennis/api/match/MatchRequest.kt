package com.example.android_frontend_tennis.api.match

import com.example.android_frontend_tennis.MatchCard
import com.example.android_frontend_tennis.PenaltyClass
import kotlinx.datetime.Instant

import kotlinx.datetime.LocalDateTime


import kotlin.collections.ArrayList


data class MatchRequestInsertOrUpdate(
    var id: String,
    val created: String,
    val setCount: Int,
    val endType: String,
    val whoServiceFirst:Int,

    val firstPlayerName:String,
    val secondPlayerName:String,
    var penalties: ArrayList<PenaltyClass>,
    var sets: Pair<ArrayList<Int>,ArrayList<Int>>,
    var points: Pair<String,String>,
)
