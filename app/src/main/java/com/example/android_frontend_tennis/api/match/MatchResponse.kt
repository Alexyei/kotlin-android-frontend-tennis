package com.example.android_frontend_tennis.api.match

import com.example.android_frontend_tennis.PenaltyClass
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant
import java.util.*
import kotlin.collections.ArrayList

data class MatchResponse (
    @SerializedName("id")
    var id: String,
    @SerializedName("created")
    val created: String,

    @SerializedName("setCount")
    val setCount: Int,

    @SerializedName("endType")
    val endType: String,

    @SerializedName("whoServiceFirst")
    val whoServiceFirst:Int,

    @SerializedName("firstPlayerName")
    val firstPlayerName:String,

    @SerializedName("secondPlayerName")
    val secondPlayerName:String,
//    var firstPlayerSets:ArrayList<Int>,
//    var secondPlayerSets:ArrayList<Int>,
//    var firstPlayerPoints:String,
//    var secondPlayerPoints: String
    @SerializedName("penalties")
    var penalties: ArrayList<PenaltyClass>,
    @SerializedName("sets")
    var sets: Pair<ArrayList<Int>,ArrayList<Int>>,
    @SerializedName("points")
    var points: Pair<String,String>,
)
