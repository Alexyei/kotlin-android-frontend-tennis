package com.example.android_frontend_tennis

import android.app.Application
import android.util.Log



//class GlobalVariables:Application() {
//    lateinit var matchCardAdapter:MatchCardAdapter
//
//    override fun onCreate(){
//        super.onCreate()
//
//        matchCardAdapter = initMatchCardList()
//    }
//}

class GlobalVariablesInstance {
    fun initMatchCardList():MatchCardAdapter{
        Log.e("Init","match car init")
        return MatchCardAdapter(mutableListOf())
    }
    companion object {


        private var matchCardAdapter: MatchCardAdapter? = null

        fun getMatchCardAdapterInstance():MatchCardAdapter{
            if (matchCardAdapter!=null)
                return matchCardAdapter as MatchCardAdapter
            else return initMatchCardList()
        }
    }
}