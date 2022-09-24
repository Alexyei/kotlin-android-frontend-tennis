package com.example.android_frontend_tennis

import java.util.*
import kotlin.collections.ArrayList

data class MatchCard (
    var id: String?,
    val created: Date,
    val setCount: Int,
    val endType: String,
    val whoServiceFirst:Int,

    var saved:Boolean,
    val firstPlayerName:String,
    val secondPlayerName:String,
    var firstPlayerSets:ArrayList<Int>,
    var secondPlayerSets:ArrayList<Int>,
    var firstPlayerPoints:String,
    var secondPlayerPoints: String
)
