package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.android_frontend_tennis.ui.components.ProgressButton

class NewMatch : AppCompatActivity() {
    private lateinit var tvNewMatchErrorText:TextView;
    private lateinit var etFirstPlayerName:EditText;
    private lateinit var etSecondPlayerName:EditText;
    private lateinit var rgSetCount:RadioGroup;
    private lateinit var rgSetEnd:RadioGroup;
    private lateinit var rgSetWhoService:RadioGroup;
    private lateinit var btnCreateMatch: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_match)
        tvNewMatchErrorText = findViewById(R.id.tvNewMatchErrorText)
        etFirstPlayerName = findViewById(R.id.etFirstPlayerName)
        etSecondPlayerName = findViewById(R.id.etSecondPlayerName)

        rgSetCount = findViewById(R.id.rgSetCount)
        rgSetEnd = findViewById(R.id.rgSetEnd)
        rgSetWhoService = findViewById(R.id.rgSetWhoService)
        btnCreateMatch = findViewById(R.id.btnCreateMatch)

        val addCreateMatchButton = ProgressButton(this@NewMatch,btnCreateMatch,"Создать")

        tvNewMatchErrorText.text = ""

        btnCreateMatch.setOnClickListener(View.OnClickListener { v->
            if (!validateForm())return@OnClickListener;
            val setCount = getSetCount()
            val endType = getEndType()
            val whoServiceFirst  = getWhoService()
            Log.e("SERVICE", whoServiceFirst.toString())
            val firstPlayerName = etFirstPlayerName.text.toString()
            val secondPlayerName = etSecondPlayerName.text.toString()

            DataObject.addData(firstPlayerName,secondPlayerName,setCount,endType,whoServiceFirst)

            val intent = Intent(this, ListOfMatches::class.java)
            startActivity(intent)
//            Toast.makeText(this@NewMatch,"$setCount:$end:$firstPlayerName:$secondPlayerName",Toast.LENGTH_LONG).show()
        })

    }

    fun getSetCount():Int{
        val radio = findViewById<RadioButton>(rgSetCount.checkedRadioButtonId).text
        return when(radio){
            "3 сета"-> 3
            else-> 5
        }
    }

    fun getWhoService():Int{
        val radio = findViewById<RadioButton>(rgSetWhoService.checkedRadioButtonId).text
        return when(radio){
            "Первый"-> 0
            "Второй"-> 1
            else-> if (Math.random() > 0.5) 0 else 1
        }
    }

    fun getEndType():String{
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