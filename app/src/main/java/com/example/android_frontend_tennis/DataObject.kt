package com.example.android_frontend_tennis

import java.util.*
import kotlin.collections.ArrayList


object DataObject {
    var listdata = mutableListOf<MatchCard>()

    fun addData(firstPlayerName: String, secondPlayerName: String, setCount:Int, endType:String, whoServiceFirst:Int) {
        val id = null;
        val createdAt = Calendar.getInstance().time
        val firstPlayerSets = ArrayList<Int>()
        firstPlayerSets.add(0)
        val secondPlayerSets = ArrayList<Int>()
        secondPlayerSets.add(0)
        val firstPlayerPoints = "0"
        val secondPlayerPoints = "0"
        val saved = false

        listdata.add(MatchCard(id,createdAt,setCount , endType, whoServiceFirst, saved, firstPlayerName, secondPlayerName, firstPlayerSets, secondPlayerSets, firstPlayerPoints, secondPlayerPoints))
    }

    fun getAllData(): List<MatchCard> {
        return listdata
    }

    fun deleteAll(){
        listdata.clear()
    }

    fun getData(pos:Int): MatchCard {
        return listdata[pos]
    }

    fun deleteData(pos:Int){
        listdata.removeAt(pos)
    }

    fun updateData(pos:Int,firstPlayerSets:ArrayList<Int>, secondPlayerSets:ArrayList<Int>, firstPlayerPoints: String, secondPlayerPoints:String)
    {
        listdata[pos].firstPlayerSets = firstPlayerSets
        listdata[pos].secondPlayerSets = secondPlayerSets
        listdata[pos].firstPlayerPoints = firstPlayerPoints
        listdata[pos].secondPlayerPoints = secondPlayerPoints
    }

    fun updateData(pos:Int,saved:Boolean)
    {
        listdata[pos].saved = saved
    }

}