package com.example.android_frontend_tennis

import java.util.*
import kotlin.collections.ArrayList

data class MatchCard (
    val id: String?,
    val created: Date,
    val saved:Boolean,
    val firstPlayerName:String,
    val secondPlayerName:String,
    val firstPlayerSets:ArrayList<Int>,
    val secondPlayerSets:ArrayList<Int>,
    val firstPlayerPoints:String,
    val secondPlayerPoints: String
)
