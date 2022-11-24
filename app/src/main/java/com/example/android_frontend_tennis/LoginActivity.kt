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

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: View;
    private lateinit var registrationLink: TextView;
    private lateinit var errorLabel: TextView;
    private lateinit var loginEditText: EditText;
    private lateinit var passwordEditText:EditText;
    private lateinit var authService: AuthService;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        authService = ServiceManager.getAuthService(getSharedPreferences("token", MODE_PRIVATE))

        loginButton = findViewById(R.id.btnCreatePenalty)
        registrationLink = findViewById(R.id.tvEndLabel)
        errorLabel = findViewById(R.id.tvNewMatchErrorText)
        loginEditText = findViewById(R.id.etFirstPlayerName)
        passwordEditText = findViewById(R.id.etSecondPlayerName)

//        переход к форме регистрации
        registrationLink.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
//            Toast.makeText(this@LoginActivity,"Регистрация",Toast.LENGTH_LONG).show()
        })

        val progressButton = ProgressButton(this@LoginActivity,loginButton,"Войти")

//        нажимаем на кнопку войти
        loginButton.setOnClickListener(View.OnClickListener { v->

//            валидируем форму, если форма не валидна выводим сообщение и выходим из метода
            if (!validateForm()) return@OnClickListener;
//            блокируем кнопку, чтобы нельзя было нажать повторно (чтобы не было лишних запросов на сервер)
            progressButton.buttonActivated()
//            val handler = Handler()
//            handler.postDelayed(Runnable { progressButton.buttonFinished() },3000)

//            в случае успешное авторизации переходим к списку матчей
            lifecycleScope.launch{
                var result = authService.signIn(loginEditText.text.toString(), passwordEditText.text.toString())

                when (result){
                    is AuthResult.Authorized ->{
                        Toast.makeText(this@LoginActivity,"Вы авторизованы!",Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, ListOfMatches::class.java)
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

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }

}