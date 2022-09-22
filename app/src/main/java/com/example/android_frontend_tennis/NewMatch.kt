package com.example.android_frontend_tennis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.android_frontend_tennis.ui.components.ProgressButton

class NewMatch : AppCompatActivity() {
    private lateinit var tvNewMatchErrorText:TextView;
    private lateinit var etFirstPlayerName:EditText;
    private lateinit var etSecondPlayerName:EditText;
    private lateinit var rgSetCount:RadioGroup;
    private lateinit var rgSetEnd:RadioGroup;
    private lateinit var btnCreateMatch: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_match)
        tvNewMatchErrorText = findViewById(R.id.tvNewMatchErrorText)
        etFirstPlayerName = findViewById(R.id.etFirstPlayerName)
        etSecondPlayerName = findViewById(R.id.etSecondPlayerName)

        rgSetCount = findViewById(R.id.rgSetCount)
        rgSetEnd = findViewById(R.id.rgSetEnd)
        btnCreateMatch = findViewById(R.id.btnCreateMatch)

        val addCreateMatchButton = ProgressButton(this@NewMatch,btnCreateMatch,"Создать")

        tvNewMatchErrorText.text = ""

        btnCreateMatch.setOnClickListener(View.OnClickListener { v->
            if (!validateForm())return@OnClickListener;
            val setCount = getSetCount()
            val end = getEnd()
            val firstPlayerName = etFirstPlayerName.text.toString()
            val secondPlayerName = etSecondPlayerName.text.toString()

            Toast.makeText(this@NewMatch,"$setCount:$end:$firstPlayerName:$secondPlayerName",Toast.LENGTH_LONG).show()
        })

    }

    fun getSetCount():Int{
        val radio = findViewById<RadioButton>(rgSetCount.checkedRadioButtonId).text
        return when(radio){
            "3 сета"->return 3
            else->return 5
        }
    }

    fun getEnd():String{
        val radio = findViewById<RadioButton>(rgSetEnd.checkedRadioButtonId).text
        return radio.toString()
    }

    fun validateForm():Boolean{
        val firstPlayerName = etFirstPlayerName.text
        val secondPlayerName = etSecondPlayerName.text
        val errorLabel = tvNewMatchErrorText
        errorLabel.text =""

        if (firstPlayerName.isBlank()){
            errorLabel.text ="Имя первого игрока обязательно"
            return false;
        }
        if (secondPlayerName.isBlank()){
            errorLabel.text ="Имя второго игрока обязательно"
            return false;
        }

        if (firstPlayerName.length < 3 || firstPlayerName.length > 30){
            errorLabel.text ="Имя первого игрока от 3 до 30 символов"
            return false;
        }

        if (secondPlayerName.length < 3 || secondPlayerName.length > 30){
            errorLabel.text ="Имя второго игрока от 3 до 30 символов"
            return false;
        }
        return true;
    }
}