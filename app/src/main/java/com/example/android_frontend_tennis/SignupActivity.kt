package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.android_frontend_tennis.api.ServiceManager
import com.example.android_frontend_tennis.api.auth.AuthResult
import com.example.android_frontend_tennis.api.auth.AuthService
import com.example.android_frontend_tennis.ui.components.ProgressButton
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var signupButton: View;
    private lateinit var loginLink: TextView;
    private lateinit var tvSignupErrorText: TextView;
    private lateinit var etSignupUsername:EditText;
    private lateinit var etSignupPassword:EditText;
    private lateinit var etSignupPasswordRepeat:EditText;

    private lateinit var authService: AuthService;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        authService = ServiceManager.getAuthService(getSharedPreferences("token",MODE_PRIVATE))


        signupButton = findViewById(R.id.signupBtn)
        loginLink = findViewById(R.id.tvEndLabel)
        tvSignupErrorText = findViewById(R.id.tvNewMatchErrorText)
        etSignupUsername = findViewById(R.id.etFirstPlayerName)
        etSignupPassword = findViewById(R.id.etSecondPlayerName)
        etSignupPasswordRepeat = findViewById(R.id.etSignupPasswordRepeat)
        val progressButton = ProgressButton(this@SignupActivity,signupButton,"Создать аккаунт")

        loginLink.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

//        нажимаем кнопку зарегистрироваться
        signupButton.setOnClickListener(View.OnClickListener { v->
//            если форма не валидна, выходим из метода
            if (!validateForm()) return@OnClickListener;
//            блокируем кнопку, чтобы небыло новых запросов к серверу
                progressButton.buttonActivated()
//            val handler = Handler()
//            handler.postDelayed(Runnable { progressButton.buttonFinished() },3000)
            lifecycleScope.launch{
                var result = authService.signUp(etSignupUsername.text.toString(), etSignupPassword.text.toString(), etSignupPasswordRepeat.text.toString())
                val errorLabel = tvSignupErrorText

                when (result){
                    is AuthResult.Authorized ->{
                        Toast.makeText(this@SignupActivity,"Вы авторизованы!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignupActivity, ListOfMatches::class.java)
                        startActivity(intent)
                    }
                    //                    маршрут на сервере не имеет auth-middleware поэтому он не может вернуть такой результат

                    is AuthResult.Unauthorized ->{
                        errorLabel.text = "Непредвиденная ошибка"
                    }
                    is AuthResult.UnknownError ->{
                        if (result.message != null){
                            errorLabel.text = result.message
                        }else{
                            errorLabel.text = "Непредвиденная ошибка"
                        }
                    }
                }


                progressButton.buttonFinished()
            }
        })

    }

    fun validateForm():Boolean{
        val login = etSignupUsername.text
        val password = etSignupPassword.text
        val repeat_password = etSignupPasswordRepeat.text
        val errorLabel = tvSignupErrorText


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

        if (!password.toString().equals(repeat_password.toString())){
            errorLabel.text ="Пароли должны совпадать!"
            return false;
        }

        return true;
    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}