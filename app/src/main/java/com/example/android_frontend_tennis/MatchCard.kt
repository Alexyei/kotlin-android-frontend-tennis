package com.example.android_frontend_tennis



import kotlinx.datetime.Instant
import java.time.LocalDateTime
import kotlin.collections.ArrayList


data class MatchCard (
    var id: String?,
    val created: Instant,
    val setCount: Int,
    val endType: String,
    val whoServiceFirst:Int,

    var saved:Boolean,
    val firstPlayerName:String,
    val secondPlayerName:String,
//    var firstPlayerSets:ArrayList<Int>,
//    var secondPlayerSets:ArrayList<Int>,
//    var firstPlayerPoints:String,
//    var secondPlayerPoints: String
    var penalties: ArrayList<PenaltyClass>,
    var sets: Pair<ArrayList<Int>,ArrayList<Int>>,
    var points: Pair<String,String>,
)
