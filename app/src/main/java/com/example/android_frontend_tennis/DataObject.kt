package com.example.android_frontend_tennis


import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.collections.ArrayList


object DataObject {
    var listdata = mutableListOf<MatchCard>()

    fun addData(firstPlayerName: String, secondPlayerName: String, setCount:Int, endType:String, whoServiceFirst:Int) {
        val id = null;
        val createdAt = Clock.System.now()
        val firstPlayerSets = ArrayList<Int>()
        firstPlayerSets.add(0)
        val secondPlayerSets = ArrayList<Int>()
        secondPlayerSets.add(0)

        val sets = Pair(firstPlayerSets, secondPlayerSets)
//        val firstPlayerPoints = "0"
//        val secondPlayerPoints = "0"

        val points = Pair("0","0")

        val penalties = ArrayList<PenaltyClass>()
        val saved = false

        listdata.add(MatchCard(id,createdAt,setCount , endType, whoServiceFirst, saved, firstPlayerName, secondPlayerName, penalties, sets,points))
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

//    fun updateData(pos:Int,firstPlayerSets:ArrayList<Int>, secondPlayerSets:ArrayList<Int>, firstPlayerPoints: String, secondPlayerPoints:String)
//    {
//        listdata[pos].firstPlayerSets = firstPlayerSets
//        listdata[pos].secondPlayerSets = secondPlayerSets
//        listdata[pos].firstPlayerPoints = firstPlayerPoints
//        listdata[pos].secondPlayerPoints = secondPlayerPoints
//    }

    fun updateDataSets(pos: Int, sets: Pair<ArrayList<Int>,ArrayList<Int>>){
        listdata[pos].sets = sets
    }

    fun updateDataPoints(pos:Int, points: Pair<String,String>){
        listdata[pos].points = points
    }

    fun updateData(pos:Int,saved:Boolean)
    {
        listdata[pos].saved = saved
    }

    fun addPenalty(pos:Int, penalty:PenaltyClass){
        listdata[pos].penalties.add(penalty)
    }

}