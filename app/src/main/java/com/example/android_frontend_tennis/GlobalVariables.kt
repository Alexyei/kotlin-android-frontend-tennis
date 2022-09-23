package com.example.android_frontend_tennis

import android.app.Application
import android.util.Log

fun initMatchCardList():MatchCardAdapter{
    Log.e("Init","match car init")
    return MatchCardAdapter(mutableListOf())
}

class GlobalVariables:Application() {
    var matchCardAdapter:MatchCardAdapter? = null;

    override fun onCreate(){
        super.onCreate()

        matchCardAdapter = initMatchCardList()
    }
}

//class GlobalVariables:Application() {
//    companion object {
//        private var matchCardAdapter: MatchCardAdapter? = null
//
//        fun getMatchCardAdapterInstance():MatchCardAdapter{
//            if (matchCardAdapter!=null)
//                return matchCardAdapter as MatchCardAdapter
//            else return initMatchCardList()
//        }
//    }
//}