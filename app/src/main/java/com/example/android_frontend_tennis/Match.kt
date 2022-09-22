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

class Match : AppCompatActivity() {

    private lateinit var tvFirstPlayerName:TextView
    private lateinit var tvSecondPlayerName:TextView
    private lateinit var ivFistPlayerServing:ImageView
    private lateinit var ivSecondPlayerServing:ImageView

    private lateinit var tvFirstPlayerSets:TextView
    private lateinit var tvFirstPlayerPoints:TextView
    private lateinit var tvSecondPlayerSets:TextView
    private lateinit var tvSecondPlayerPoints:TextView

    private lateinit var tvServingTimer:TextView;
    private lateinit var tvMessage:TextView;

    private lateinit var btnRemovePointToFirstPlayer: View;
    private lateinit var btnAddPointToFirstPlayer:View;

    private lateinit var btnRemovePointToSecondPlayer:View;
    private lateinit var btnAddPointToSecondPlayer:View;
    private lateinit var btnStartServing:View;
    private lateinit var btnCreateMatch:View;

    private lateinit var countDownTimer:CountDownTimer;
    private var timeLeftInMilliseconds:Long = 20*1000;
    private var timerRunning = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        tvFirstPlayerName = findViewById(R.id.tvFirstPlayerName)
        tvSecondPlayerName = findViewById(R.id.tvSecondPlayerName)
        ivFistPlayerServing = findViewById(R.id.ivFistPlayerServing)
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


        ProgressButton(this@Match,btnStartServing,"Подача")
        ProgressButton(this@Match,btnCreateMatch,"Оштрафовать")

        tvMessage.visibility = View.GONE
        ivFistPlayerServing.visibility = View.GONE
        ivSecondPlayerServing.visibility = View.GONE

        tvServingTimer.text ="00.00"

        btnRemovePointToFirstPlayer.setOnClickListener(View.OnClickListener { v->
            Toast.makeText(this@Match,"- 1",Toast.LENGTH_LONG).show()
        })

        btnRemovePointToSecondPlayer.setOnClickListener(View.OnClickListener { v->
            Toast.makeText(this@Match,"- 2",Toast.LENGTH_LONG).show()

        })

        btnAddPointToFirstPlayer.setOnClickListener(View.OnClickListener { v->
            Toast.makeText(this@Match,"+ 1",Toast.LENGTH_LONG).show()

        })

        btnAddPointToSecondPlayer.setOnClickListener(View.OnClickListener { v->
            Toast.makeText(this@Match,"+ 2",Toast.LENGTH_LONG).show()

        })

        btnStartServing.setOnClickListener(View.OnClickListener { v->
            startStopServingTimer()
        })

        btnCreateMatch.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(this, Penalty::class.java)
            startActivity(intent)
        })
    }

    fun startStopServingTimer(){
        fun updateTimer(millisUntilFinished: Long){
            val seconds = millisUntilFinished / 1000
            val milli_seconds = millisUntilFinished % 1000 / 10

            var timeLeftText =  if (seconds < 10) "0" else ""
            timeLeftText+="${seconds.toString()}.${milli_seconds.toString().padStart(2,'0')}"

            tvServingTimer.text = timeLeftText
        }

        fun startTimer(){
            countDownTimer = object: CountDownTimer(timeLeftInMilliseconds, 10){
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

        fun stopTimer(){
            countDownTimer.cancel()
            updateTimer(0)
            timerRunning = false;
        }

        if (timerRunning){
            stopTimer()
        }else{
            startTimer()
        }


    }
}