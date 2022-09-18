package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.android_frontend_tennis.ui.components.ProgressButton

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: View;
    private lateinit var registrationLink: TextView;
    private lateinit var errorLabel: TextView;
    private lateinit var loginEditText: EditText;
    private lateinit var passwordEditText:EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        loginButton = findViewById(R.id.loginBtn)
        registrationLink = findViewById(R.id.tvSignupLoginLink)
        errorLabel = findViewById(R.id.tvSignupErrorText)
        loginEditText = findViewById(R.id.etSignupUsername)
        passwordEditText = findViewById(R.id.etSignupPassword)

        registrationLink.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
//            Toast.makeText(this@LoginActivity,"Регистрация",Toast.LENGTH_LONG).show()
        })

        val progressButton = ProgressButton(this@LoginActivity,loginButton,"Войти")

        loginButton.setOnClickListener(View.OnClickListener { v->

            if (!validateForm()) return@OnClickListener;
            progressButton.buttonActivated()
            val handler = Handler()
            handler.postDelayed(Runnable { progressButton.buttonFinished() },3000)
        })
    }

    fun validateForm():Boolean{
        val login = loginEditText.text
        val password = passwordEditText.text

        errorLabel.text =""
            if (login.isBlank()){
                errorLabel.text ="Логин обязателен"
            return false;
        }
        if (password.isBlank()){
            errorLabel.text ="Пароль обязателен"
            return false;
        }
        if (login.length < 3 || login.length > 10){
            errorLabel.text ="Логин от 3 до 10 символов"
            return false;
        }
        if (password.length < 4 || password.length > 7){
            errorLabel.text ="Пароль от 4 до 7 символов"
            return false;
        }

        return true;
    }
}