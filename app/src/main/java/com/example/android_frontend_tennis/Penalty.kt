package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.android_frontend_tennis.ui.components.ProgressButton

class Penalty : AppCompatActivity() {
    private lateinit var rgSetCause:RadioGroup
    private lateinit var rgSetPenalty:RadioGroup
    private lateinit var rgSetPlayer:RadioGroup
    private lateinit var rbFirstPlayer:RadioButton
    private lateinit var rbSecondPlayer:RadioButton
    private lateinit var etmCause:EditText
    private lateinit var btnAddPenalty:View

    private lateinit var currentCard: MatchCard;
    private var cardPosition: Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penalty)

        rgSetCause = findViewById(R.id.rgSetCause)
        rgSetPenalty = findViewById(R.id.rgSetPenalty)
        rgSetPlayer = findViewById(R.id.rgSetPlayer)

        rbFirstPlayer = findViewById(R.id.rbFirstPlayer)
        rbSecondPlayer = findViewById(R.id.rbSecondPlayer)
        etmCause = findViewById(R.id.etmCause)
        btnAddPenalty = findViewById(R.id.btnAddPenalty)

        val addPenaltyButton = ProgressButton(this@Penalty,btnAddPenalty,"Применить")


        cardPosition = intent.getIntExtra("position", -1)

        if (cardPosition == -1) {
            val intent = Intent(this, ListOfMatches::class.java)
            startActivity(intent)
        }

        currentCard = DataObject.getData(cardPosition)

        rbFirstPlayer.text = currentCard.firstPlayerName
        rbSecondPlayer.text = currentCard.secondPlayerName


        rgSetCause.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            if (radio !== null) {
                etmCause.setText(radio.text.toString())
            }
        }

        btnAddPenalty.setOnClickListener(View.OnClickListener { view->
            val cause = etmCause.text.toString()
            val penalty = findViewById<RadioButton>(rgSetPenalty.checkedRadioButtonId).text.toString()
            val toFirstPlayer =
                findViewById<RadioButton>(rgSetPlayer.checkedRadioButtonId).text == currentCard.firstPlayerName;

            Toast.makeText(this@Penalty,"$cause:$penalty:$toFirstPlayer",Toast.LENGTH_LONG).show()


            DataObject.addPenalty(cardPosition, PenaltyClass(cause,penalty,toFirstPlayer))

            val intent = Intent()
            intent.putExtra("penalty position", currentCard.penalties.count() - 1);
            setResult(RESULT_OK, intent);
            finish();
        })

    }

//    override fun onBackPressed() {
//        // Do Here what ever you want do on back press;
////        val intent = Intent(this, Match::class.java)
////        intent.putExtra("position", cardPosition);
////        startActivity(intent)
//    }
}