package com.example.android_frontend_tennis

import com.google.gson.annotations.SerializedName

data class PenaltyClass(
    @SerializedName("cause")
    val cause: String = "",
    @SerializedName("penalty")
    val penalty: String,
    @SerializedName("toFirstPlayer")
    val toFirstPlayer:Boolean
)
