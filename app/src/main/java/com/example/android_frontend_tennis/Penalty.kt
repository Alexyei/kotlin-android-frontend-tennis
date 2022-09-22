package com.example.android_frontend_tennis

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
    private lateinit var etmCause:EditText
    private lateinit var btnAddPenalty:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penalty)

        rgSetCause = findViewById(R.id.rgSetCause)
        rgSetPenalty = findViewById(R.id.rgSetPenalty)
        etmCause = findViewById(R.id.etmCause)
        btnAddPenalty = findViewById(R.id.btnAddPenalty)

        val addPenaltyButton = ProgressButton(this@Penalty,btnAddPenalty,"Применить")

        rgSetCause.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton? = findViewById(checkedId)
            if (radio !== null) {
                etmCause.setText(radio.text.toString())
            }
        }

        btnAddPenalty.setOnClickListener(View.OnClickListener { view->
            val cause = etmCause.text
            val penalty = findViewById<RadioButton>(rgSetPenalty.checkedRadioButtonId).text

            Toast.makeText(this@Penalty,"$cause:$penalty",Toast.LENGTH_LONG).show()
        })

    }
}