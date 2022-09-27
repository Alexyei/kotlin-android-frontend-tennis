package com.example.android_frontend_tennis

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android_frontend_tennis.ui.components.ProgressButton
import kotlin.math.max
import kotlin.math.min

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
    private lateinit var btnCreatePenalty: View;

    private lateinit var countDownTimer: CountDownTimer;
    private var timeLeftInMilliseconds: Long = 20 * 1000;
    private var timerRunning = false;

    private lateinit var currentCard: MatchCard;
    private var cardPosition: Int = -1;

    val getPenalty = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val penalty = it?.data?.getIntExtra("penalty position",-1)
//        Toast.makeText(this,penalty,Toast.LENGTH_LONG).show()
        Log.e("PENALTY", penalty.toString())
        if (penalty !== null && penalty != -1){

            val currentPenalty = currentCard.penalties[penalty]

//            Log.e("PENALTY", currentPenalty.penalty)

            when(currentPenalty.penalty){
                "+1 очко"->{
                    addPoint(!currentPenalty.toFirstPlayer)
                }
                "+1 гейм"->{
                    plusGame(!currentPenalty.toFirstPlayer)
                }
                "+1 сет"->{
                    plusSet(!currentPenalty.toFirstPlayer)
                }
//                "дисквалификация"->{}
            }
            rerenderLabels()
        }


    }

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
        btnCreatePenalty = findViewById(R.id.btnCreatePenalty)


        ProgressButton(this@Match, btnStartServing, "Подача")
        ProgressButton(this@Match, btnCreatePenalty, "Оштрафовать")


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
            removePoint(true)
            rerenderLabels()
        })

        btnRemovePointToSecondPlayer.setOnClickListener(View.OnClickListener { v ->
            removePoint(false)
            rerenderLabels()
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

        btnCreatePenalty.setOnClickListener(View.OnClickListener { v ->

            if (!isMatchOver()) {

//            val intent = Intent(this, Penalty::class.java)
                val intent = Intent("action.getPenalty")
                intent.putExtra("position", cardPosition);

                getPenalty.launch(intent)
//            startActivity(intent)
            }
        })
    }

    fun rerenderSetAndPointsLabel() {
        val sets = currentCard.sets
        val points = currentCard.points

        tvFirstPlayerSets.text = sets.first.joinToString(" ")
        tvSecondPlayerSets.text = sets.second.joinToString(" ")

        tvFirstPlayerPoints.text = points.first
        tvSecondPlayerPoints.text = points.second
    }

    fun resetServiceLabel() {
        ivFirstPlayerServing.visibility = View.GONE
        ivSecondPlayerServing.visibility = View.GONE
    }


    fun isTieBreakNow(): Boolean {
        val sets = currentCard.sets
        if (currentCard.endType == "+2 games" && sets.first.count() == currentCard.setCount) return false;

        return sets.first.last() == 6 && sets.second.last() == 6;
    }

    fun isFirstGame(): Boolean {
        val sets = currentCard.sets

        return (sets.first.count() == 1 && sets.first.last() == 0 && sets.second.last() == 0)
    }

    fun rerenderServiceLabel() {
        val sets = currentCard.sets
        val points = currentCard.points

        resetServiceLabel()


        fun whoServiceInLastGame(): Int {
            val gamesCount = sets.first.sum() + sets.second.sum()
            val whoServiceLastGame = gamesCount % 2

            val whoServiceFirst = currentCard.whoServiceFirst
            val whoServiceSecond = if (whoServiceFirst == 1) 0 else 1;

            if (whoServiceLastGame == 1) {
                return whoServiceFirst
            }
            return whoServiceSecond
        }

        fun whoNotServiceInLastGame(): Int {
            return if (whoServiceInLastGame() == 0) 1 else 0;
        }

        fun renderServiceLabel(whoServiceNow: Int) {
            if (whoServiceNow == 0) {
                ivFirstPlayerServing.visibility = View.VISIBLE
            } else {
                ivSecondPlayerServing.visibility = View.VISIBLE
            }
        }

        if (isTieBreakNow()) {
            val firstPlayerPointsValue = points.first.toInt()
            val secondPlayerPointsValue = points.second.toInt()

            val pointCount = firstPlayerPointsValue + secondPlayerPointsValue

            if (pointCount == 0) {
                renderServiceLabel(whoNotServiceInLastGame())
            } else {
                if ((pointCount + 1) % 4 == 0 || pointCount % 4 == 0) {
                    renderServiceLabel(whoNotServiceInLastGame())
                } else {
                    renderServiceLabel(whoServiceInLastGame())
                }
            }
        } else {
            renderServiceLabel(whoNotServiceInLastGame())
        }
    }


    fun getMatchResult(): Pair<Int, Int> {
        val sets = currentCard.sets

        var result = Pair(0, 0)

        for (i in 0..sets.first.count() - 1) {
            if (sets.first[i] > sets.second[i])
                result = Pair(result.first + 1, result.second)
            else
                result = Pair(result.first, result.second + 1)
        }

        return result;
    }

    fun longGameIsOver(): Boolean {

        if (currentCard.endType == "+2 games") {
            val sets = currentCard.sets
            val different = max(sets.first.last(), sets.second.last()) - min(
                sets.first.last(),
                sets.second.last()
            )

            if ((sets.first.count() == currentCard.setCount) && max(
                    sets.first.last(),
                    sets.second.last()
                ) >= 7 && different < 2
            ) return false
        }

        return true;
    }

    fun currentSetIsOver(): Boolean {
        val sets = currentCard.sets




        if ((sets.first.last() >= 7 || sets.second.last() >= 7) && longGameIsOver()) return true
        if (sets.first.last() == 6 && sets.second.last() < 5) return true
        if (sets.second.last() == 6 && sets.first.last() < 5) return true

        return false;
    }

    fun isMatchOver(): Boolean {

        //CHECK DISQUALIFICATION
        if (currentCard.penalties.any { p -> p.penalty == "дисквалификация" })
            return true;

        if (currentSetIsOver()) {
            val result = getMatchResult()
            val setCount = currentCard.setCount
            val setEndCount = result.first + result.second
            val different =
                Math.max(result.first, result.second) - Math.min(result.first, result.second)

            if (setCount - setEndCount < different) return true;

        }

        return false;
    }

    fun renderWinner() {
        val result = getMatchResult()

        //CHECK DISQUALIFICATION
        val disqualificationPenalties = currentCard.penalties.filter { p -> p.penalty == "дисквалификация" }
        if (disqualificationPenalties.count() > 0){
            val disqualificatedPlayer = disqualificationPenalties.first().toFirstPlayer

            if (disqualificatedPlayer){
                tvMessage.text = "${tvFirstPlayerName.text} дисквалифицирован"
            }
            else{
                tvMessage.text = "${tvSecondPlayerName.text} дисквалифицирован"
            }

            return;
        }

        if (result.first > result.second) {
            tvMessage.text = "Победил ${tvFirstPlayerName.text}"
        } else {
            tvMessage.text = "Победил ${tvSecondPlayerName.text}"
        }
    }

    fun rerenderMessageLabel() {
        tvMessage.text = ""

        // CHECK смена сторон
        if (isChangeOfSidesTime())
            tvMessage.text = "Смена сторон!"
        // CHECK новые мячи
        if (isChangeBallTime())
            tvMessage.text = tvMessage.text.toString()+" Смена мячей!"
    }

    fun disablePlusBtns() {
        btnAddPointToFirstPlayer.visibility = View.GONE
        btnAddPointToSecondPlayer.visibility = View.GONE
    }

    fun enablePlusBtns() {
        btnAddPointToFirstPlayer.visibility = View.VISIBLE
        btnAddPointToSecondPlayer.visibility = View.VISIBLE
    }

    fun disableMinusBtns() {
        btnRemovePointToFirstPlayer.visibility = View.GONE
        btnRemovePointToSecondPlayer.visibility = View.GONE
    }

    fun enableMinusBtns() {
        btnRemovePointToFirstPlayer.visibility = View.VISIBLE
        btnRemovePointToSecondPlayer.visibility = View.VISIBLE
    }

    fun isFirstPoint(): Boolean {
        val sets = currentCard.sets
        if (sets.first.count() == 1 && sets.first.last().toInt() == 0 && sets.second.last().toInt() == 0) {
            val points = currentCard.points;
            if (points.first == "0" && points.second == "0")
                return true;
        }

        return false;
    }

    fun rerenderLabels() {
        rerenderSetAndPointsLabel()
        if (!isMatchOver()) {
            rerenderServiceLabel()
            rerenderMessageLabel()
            enablePlusBtns()


        } else {
            resetServiceLabel()
            renderWinner()
            disablePlusBtns()
        }

        if (isFirstPoint()) {
            disableMinusBtns()
        } else {
            enableMinusBtns()
        }
    }

    fun addSet() {
        val setCount = currentCard.setCount
        var sets = currentCard.sets
        if (!isMatchOver()) {
            sets.first.add(0)
            sets.second.add(0)
            DataObject.updateDataSets(cardPosition, sets)
        }
    }

    fun addGame(toFirstPlayer: Boolean) {
        var sets = currentCard.sets
        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)


        sets.first[sets.first.count() - 1] = sets.first.last() + 1

        val currentGamesValue = sets.first.last()
        val opponentGamesValue = sets.second.last()



        if (currentGamesValue == 7 && longGameIsOver()) {
            addSet()
        } else if (currentGamesValue == 6 && opponentGamesValue < 5) {
            addSet()
        }

        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)

        DataObject.updateDataSets(cardPosition, sets)
    }

    fun addPoint(toFirstPlayer: Boolean) {
        var points = currentCard.points
        if (!toFirstPlayer) points = Pair(points.second, points.first)


        if (isTieBreakNow()) {
            val firstPointValue = (points.first.toInt() + 1)
            val secondPointValue = points.second.toInt()
            val nextPoint = firstPointValue.toString()
            val criticalPoint = if (currentCard.endType == "super TB") 10 else 7;
            if (firstPointValue - secondPointValue > 1 && firstPointValue >= criticalPoint) {
                points = Pair("0", "0")
                addGame(toFirstPlayer)
            } else {
                points = Pair(nextPoint, points.second)
            }
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
                "40" -> {
                    if (points.second == "40") {
                        points = Pair("AD", "")
                    } else {
                        points = Pair("0", "0")
                        addGame(toFirstPlayer)
                    }
                }
                "AD" -> {
                    points = Pair("0", "0")
                    addGame(toFirstPlayer)
                }
            }
        }

        if (!toFirstPlayer) points = Pair(points.second, points.first)

        DataObject.updateDataPoints(cardPosition, points)
    }

    fun plusSet(toFirstPlayer: Boolean){
        var points = currentCard.points
        var sets = currentCard.sets
        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)

        points = Pair("0","0")
        val currentGamesValue = sets.first.last()
        val opponentGamesValue = sets.second.last()

        if (opponentGamesValue <= 4){
            sets.first[sets.first.count() - 1] = 6
        }
        else{
            sets.first[sets.first.count() - 1] = opponentGamesValue + 2
        }

        addSet()


        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)
        DataObject.updateDataPoints(cardPosition, points)
        DataObject.updateDataSets(cardPosition, sets)
    }

    fun plusGame(toFirstPlayer: Boolean) {
        var points = currentCard.points
        if (!toFirstPlayer) points = Pair(points.second, points.first)

        points = Pair("0","0")
        addGame(toFirstPlayer)

        if (!toFirstPlayer) points = Pair(points.second, points.first)

        DataObject.updateDataPoints(cardPosition, points)
    }

    fun removeSet() {
        val setCount = currentCard.setCount
        var sets = currentCard.sets
        if (sets.first.count() > 1) {
            sets.first.removeLast()
            sets.second.removeLast()
            DataObject.updateDataSets(cardPosition, sets)
        }
    }

    fun removeGame(toFirstPlayer: Boolean) {
        var sets = currentCard.sets
        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)


//        sets.first[sets.first.count() - 1] = sets.first.last() - 1

        val currentGamesValue = sets.first.last()
        val opponentGamesValue = sets.second.last()


        if (currentGamesValue == 0) {
            if (opponentGamesValue == 0) {
                removeSet()
                removeGame(toFirstPlayer)
            }
        }
        else{
            sets.first[sets.first.count() - 1] = sets.first.last() - 1
        }


//        if (currentGamesValue == 7 && longGameIsOver()){
//            addSet()
//        }
//        else if (currentGamesValue == 6 && opponentGamesValue < 5){
//            addSet()
//        }

        if (!toFirstPlayer) sets = Pair(sets.second, sets.first)

        DataObject.updateDataSets(cardPosition, sets)
    }

    fun removePoint(toFirstPlayer: Boolean) {
        var points = currentCard.points
        if (!toFirstPlayer) points = Pair(points.second, points.first)

        if (isTieBreakNow()) {
            val firstPointValue = (points.first.toInt())
            val secondPointValue = points.second.toInt()
            val nextPoint = firstPointValue.toString()
            val criticalPoint = if (currentCard.endType == "super TB") 10 else 7;

            if (firstPointValue == 0) {
                removeGame(toFirstPlayer)
                points = Pair("0", "0")
            }

//            else if (secondPointValue >= criticalPoint && secondPointValue > firstPointValue){
//                removeGame(toFirstPlayer)
//                points = Pair("0","0")
//            }
            else {
                points = Pair((firstPointValue - 1).toString(), secondPointValue.toString())
            }
        } else {
            when (points.first) {
                "" -> {
                    points = Pair("30", "40")
//                    removeGame(toFirstPlayer)
                }
                "0" -> {
                    points = Pair("0", "0")
                    removeGame(toFirstPlayer)
                }
                "15" -> {
                    points = Pair("0", points.second)
                }
                "30" -> {
                    points = Pair("15", points.second)
                }
                "40" -> {
                    points = Pair("30", points.second)
                }
                "AD" -> {
                    points = Pair("40", "40")
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

    fun isChangeOfSidesTime():Boolean{
        val points = currentCard.points

        if (isTieBreakNow()){
            val sumOfPoints = points.first.toInt() + points.second.toInt()
            // смена сторон через каждые 6 очков на тай-брейке
            if (sumOfPoints != 0 && sumOfPoints % 6 == 0){
                return true;
            }
        }else if (points.first=="0" && points.second=="0"){
            val points = currentCard.points
            val sets = currentCard.sets

//            Смена сторон в начале сета,если было сыграно нечётное количество геймой
            if (sets.first.last() == 0 && sets.second.last() == 0){
                if (sets.first.count() > 1){
                    return true;
                }
            }
            else{
                val gamesCount = sets.first.last() + sets.second.last()

                if ((gamesCount%2)==1) return true
            }
        }

        return false;
    }

    fun isChangeBallTime():Boolean{
        //СМЕНА МЯЧЕЙ ЧЕРЕЗ КАЖДЫЕ 7 геймов
        val points = currentCard.points
        if (points.first=="0" && points.second=="0"){
            val sets = currentCard.sets
            val setsSum = sets.first.sum() + sets.second.sum()

            if ((setsSum % 7)==0) return true;
        }

        return false;

    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
        val intent = Intent(this, ListOfMatches::class.java)
        startActivity(intent)
    }
}