package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.android_frontend_tennis.ui.components.ProgressButton
import org.json.JSONArray

class Match : AppCompatActivity() {

    private lateinit var tvFirstPlayerName: TextView
    private lateinit var tvSecondPlayerName: TextView
    private lateinit var ivFirstPlayerServing: ImageView
    private lateinit var ivSecondPlayerServing: ImageView

    private lateinit var tvFirstPlayerSets: TextView
    private lateinit var tvFirstPlayerPoints: TextView
    private lateinit var tvSecondPlayerSets: TextView
    private lateinit var tvSecondPlayerPoints: TextView

    private lateinit var tvServingTimer: TextView;
    private lateinit var tvMessage: TextView;

    private lateinit var btnRemovePointToFirstPlayer: View;
    private lateinit var btnAddPointToFirstPlayer: View;

    private lateinit var btnRemovePointToSecondPlayer: View;
    private lateinit var btnAddPointToSecondPlayer: View;
    private lateinit var btnStartServing: View;
    private lateinit var btnCreateMatch: View;

    private lateinit var countDownTimer: CountDownTimer;
    private var timeLeftInMilliseconds: Long = 20 * 1000;
    private var timerRunning = false;

    private lateinit var currentCard:MatchCard;
    private var cardPosition:Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        tvFirstPlayerName = findViewById(R.id.tvFirstPlayerName)
        tvSecondPlayerName = findViewById(R.id.tvSecondPlayerName)
        ivFirstPlayerServing = findViewById(R.id.ivFirstPlayerServing)
        ivSecondPlayerServing = findViewById(R.id.ivSecondPlayerServing)

        tvFirstPlayerSets = findViewById(R.id.tvFirstPlayerSets)
        tvSecondPlayerSets = findViewById(R.id.tvSecondPlayerSets)
        tvFirstPlayerPoints = findViewById(R.id.tvFirstPlayerPoints)
        tvSecondPlayerPoints = findViewById(R.id.tvSecondPlayerPoints)

        tvServingTimer = findViewById(R.id.tvServingTimer)
        tvMessage = findViewById(R.id.tvMessage)

        btnRemovePointToFirstPlayer = findViewById(R.id.btnRemovePointToFirstPlayer)
        btnRemovePointToSecondPlayer = findViewById(R.id.btnRemovePointToSecondPlayer)
        btnAddPointToFirstPlayer = findViewById(R.id.btnAddPointToFirstPlayer)
        btnAddPointToSecondPlayer = findViewById(R.id.btnAddPointToSecondPlayer)

        btnStartServing = findViewById(R.id.btnStartServing)
        btnCreateMatch = findViewById(R.id.btnCreateMatch)


        ProgressButton(this@Match, btnStartServing, "Подача")
        ProgressButton(this@Match, btnCreateMatch, "Оштрафовать")


        ivFirstPlayerServing.visibility = View.GONE
        ivSecondPlayerServing.visibility = View.GONE

        tvServingTimer.text = "00.00"

        cardPosition = intent.getIntExtra("position", -1)
//        val matchCardAdapter = GlobalVariablesInstance.getMatchCardAdapterInstance()
//        val matchCardAdapter = GlobalVariables().matchCardAdapter as MatchCardAdapter
//
        if (cardPosition == -1) {
            val intent = Intent(this, ListOfMatches::class.java)
            startActivity(intent)
        }


        currentCard = DataObject.getData(cardPosition)
//
        tvFirstPlayerName.text = currentCard.firstPlayerName
        tvSecondPlayerName.text = currentCard.secondPlayerName


        rerenderLabels()



        btnRemovePointToFirstPlayer.setOnClickListener(View.OnClickListener { v ->
            Toast.makeText(this@Match, "- 1", Toast.LENGTH_LONG).show()
        })

        btnRemovePointToSecondPlayer.setOnClickListener(View.OnClickListener { v ->
            Toast.makeText(this@Match, "- 2", Toast.LENGTH_LONG).show()

        })

        btnAddPointToFirstPlayer.setOnClickListener(View.OnClickListener { v ->
            addPoint(true)
            rerenderLabels()
        })

        btnAddPointToSecondPlayer.setOnClickListener(View.OnClickListener { v ->
            addPoint(false)
            rerenderLabels()
        })

        btnStartServing.setOnClickListener(View.OnClickListener { v ->
            startStopServingTimer()
        })

        btnCreateMatch.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(this, Penalty::class.java)
            startActivity(intent)
        })
    }

    fun rerenderSetAndPointsLabel(){
        val sets = currentCard.sets
        val points = currentCard.points

        tvFirstPlayerSets.text = sets.first.joinToString(" ")
        tvSecondPlayerSets.text = sets.second.joinToString(" ")

        tvFirstPlayerPoints.text = points.first
        tvSecondPlayerPoints.text = points.second
    }

    fun resetServiceLabel(){
        ivFirstPlayerServing.visibility = View.GONE
        ivSecondPlayerServing.visibility = View.GONE
    }


    fun isTieBreakNow(): Boolean {
        val sets = currentCard.sets
        if (currentCard.endType == "+2 games" && sets.first.count() == currentCard.setCount) return false;

        return sets.first.last() ==6 && sets.second.last() == 6;
    }

    fun isFirstGame():Boolean{
        val sets = currentCard.sets

        return (sets.first.count() == 1 && sets.first.last() == 0 && sets.second.last() == 0 )
    }

    fun rerenderServiceLabel(){
        val sets = currentCard.sets
        val points = currentCard.points

        resetServiceLabel()


        fun whoServiceInLastGame():Int{
            val gamesCount = sets.first.sum() + sets.second.sum()
            val whoServiceLastGame = gamesCount % 2

            val whoServiceFirst = currentCard.whoServiceFirst
            val whoServiceSecond = if (whoServiceFirst == 1)  0 else 1;

            if (whoServiceLastGame == 1)
            {
                return whoServiceFirst
//                    if (whoServiceFirst==0){
//                        ivFirstPlayerServing.visibility = View.VISIBLE
//                    }
//                    else{
//                        ivSecondPlayerServing.visibility = View.VISIBLE
//                    }
            }
//                else{
            return whoServiceSecond
//                    if (whoServiceFirst==0){
//                        ivSecondPlayerServing.visibility = View.VISIBLE
//                    }
//                    else{
//                        ivFirstPlayerServing.visibility = View.VISIBLE
//                    }
//                }
        }
        fun whoNotServiceInLastGame():Int{
            return if (whoServiceInLastGame() == 0) 1 else 0;
        }

        fun renderServiceLabel(whoServiceNow:Int){
            if (whoServiceNow==0){
                ivFirstPlayerServing.visibility = View.VISIBLE
            }
            else{
                ivSecondPlayerServing.visibility = View.VISIBLE
            }
        }

        if (isTieBreakNow()){
            val firstPlayerPointsValue = points.first.toInt()
            val secondPlayerPointsValue = points.second.toInt()

            val pointCount = firstPlayerPointsValue + secondPlayerPointsValue

            if (pointCount == 0){
                renderServiceLabel(whoNotServiceInLastGame())
            }
            else{
                if ((pointCount+1) % 4 == 0 || pointCount % 4 == 0){
                    renderServiceLabel(whoNotServiceInLastGame())
                }
                else{
                    renderServiceLabel(whoServiceInLastGame())
                }
            }
        }
        else{
//                val gamesCount = sets.first.sum() + sets.second.sum() + 1
//                val whoServiceNext = gamesCount % 2
//
//                val whoServiceFirst = 1
//
//                if (whoServiceNext == 1)
//                {
//                    if (whoServiceFirst==0){
//                        ivFirstPlayerServing.visibility = View.VISIBLE
//                    }
//                    else{
//                        ivSecondPlayerServing.visibility = View.VISIBLE
//                    }
//                }
//                else{
//                    if (whoServiceFirst==0){
//                        ivSecondPlayerServing.visibility = View.VISIBLE
//                    }
//                    else{
//                        ivFirstPlayerServing.visibility = View.VISIBLE
//                    }
//                }
//                val whoServiceNow = if (whoServiceInLastGame() == 1) 0 else 1;
//            if (isFirstGame()){
//                renderServiceLabel(currentCard.whoServiceFirst)}
//            else{
            renderServiceLabel(whoNotServiceInLastGame())
        }
    }


    fun getMatchResult():Pair<Int,Int>{
        val sets = currentCard.sets

        var result = Pair(0,0)

        for (i in 0..sets.first.count()-1){
            if (sets.first[i] > sets.second[i])
                result = Pair(result.first+1, result.second)
            else
                result = Pair(result.first, result.second+1)
        }

        return result;
    }

    fun currentSetIsOver():Boolean{
        val sets = currentCard.sets

        if (sets.first.last() == 7 || sets.second.last() == 7) return true
        if (sets.first.last() == 6 && sets.second.last() < 5) return true
        if (sets.second.last() == 6 && sets.first.last() < 5) return true

        return false;
    }

    fun isMatchOver():Boolean{

        //CHECK DISQUALIFICATION

        if (currentSetIsOver()){
            val result = getMatchResult()
            val setCount = currentCard.setCount
            val setEndCount = result.first + result.second
            val different = Math.max(result.first,result.second) - Math.min(result.first, result.second)

            if (setCount - setEndCount < different) return true;

        }

        return false;
    }

    fun renderWinner(){
        val result = getMatchResult()

        //CHECK DISQUALIFICATION



        if (result.first > result.second){
            tvMessage.text = "Победил ${tvFirstPlayerName.text}"
        }
        else{
            tvMessage.text = "Победил ${tvSecondPlayerName.text}"
        }
    }

    fun rerenderMessageLabel(){
        tvMessage.text = ""

        // CHECK смена сторон
        // CHECK новые мячи
    }

    fun disablePlusBtns(){
        btnAddPointToFirstPlayer.visibility = View.GONE
        btnAddPointToSecondPlayer.visibility = View.GONE
    }

    fun enablePlusBtns(){
        btnAddPointToFirstPlayer.visibility = View.VISIBLE
        btnAddPointToSecondPlayer.visibility = View.VISIBLE
    }

    fun rerenderLabels(){
        rerenderSetAndPointsLabel()
        if (!isMatchOver()){
            rerenderServiceLabel()
            rerenderMessageLabel()
            enablePlusBtns()
        }
        else{
            resetServiceLabel()
            renderWinner()
            disablePlusBtns()
        }
    }

    fun addSet(){
        val setCount = currentCard.setCount
        var sets = currentCard.sets
        if (!isMatchOver())
        {
            sets.first.add(0)
            sets.second.add(0)
            DataObject.updateDataSets(cardPosition,sets)
        }
    }

    fun addGame(toFirstPlayer:Boolean){
        var sets = currentCard.sets
        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)


        sets.first[sets.first.count() - 1] = sets.first.last() + 1

        val currentGamesValue = sets.first.last()
        val opponentGamesValue = sets.second.last()



        if (currentGamesValue == 7){
            addSet()
        }
        else if (currentGamesValue == 6 && opponentGamesValue < 5){
            addSet()
        }

        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)

        DataObject.updateDataSets(cardPosition,sets)
    }

    fun addPoint(toFirstPlayer: Boolean) {
        var points = currentCard.points
        if (!toFirstPlayer) points = Pair(points.second, points.first)


        if (isTieBreakNow()) {
            val firstPointValue = (points.first.toInt() + 1)
            val secondPointValue = points.second.toInt()
            val nextPoint = firstPointValue.toString()
            val criticalPoint = if (currentCard.endType == "super TB") 10 else 7;
            if (firstPointValue - secondPointValue > 1 && firstPointValue >= criticalPoint){
                points = Pair("0","0")
                addGame(toFirstPlayer)}
            else{
                points = Pair(nextPoint,points.second)}
        } else {
            when (points.first) {
                "" -> {
                    points = Pair("40", "40")
                }
                "0" -> {
                    points = Pair("15", points.second)
                }
                "15" -> {
                    points = Pair("30", points.second)
                }
                "30" -> {
                    points = Pair("40", points.second)
                }
                "40" ->{
                    if (points.second == "40") {points = Pair("AD", "")}
                    else{
                        points = Pair("0","0")
                        addGame(toFirstPlayer)
                    }
                }
                "AD" -> {
                    points = Pair("0","0")
                    addGame(toFirstPlayer)
                }
            }
        }

        if (!toFirstPlayer) points = Pair(points.second, points.first)

        DataObject.updateDataPoints(cardPosition, points)
    }

    fun startStopServingTimer() {
        fun updateTimer(millisUntilFinished: Long) {
            val seconds = millisUntilFinished / 1000
            val milli_seconds = millisUntilFinished % 1000 / 10

            var timeLeftText = if (seconds < 10) "0" else ""
            timeLeftText += "${seconds.toString()}.${milli_seconds.toString().padStart(2, '0')}"

            tvServingTimer.text = timeLeftText
        }

        fun startTimer() {
            countDownTimer = object : CountDownTimer(timeLeftInMilliseconds, 10) {
                override fun onTick(millisUntilFinished: Long) {
                    updateTimer(millisUntilFinished)
                }

                override fun onFinish() {
                    updateTimer(0)
                    timerRunning = false;
                }

            }
            countDownTimer.start()
            timerRunning = true;
        }

        fun stopTimer() {
            countDownTimer.cancel()
            updateTimer(0)
            timerRunning = false;
        }

        if (timerRunning) {
            stopTimer()
        } else {
            startTimer()
        }


    }
}