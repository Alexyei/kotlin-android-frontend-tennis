package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

        authService = ServiceManager.getAuthService(getPreferences(MODE_PRIVATE))

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
//            progressButton.buttonActivated()
//            val handler = Handler()
//            handler.postDelayed(Runnable { progressButton.buttonFinished() },3000)
            lifecycleScope.launch{
                var result = authService.signIn(loginEditText.text.toString(), passwordEditText.text.toString())

                when (result){
                    is AuthResult.Authorized ->{
                        Toast.makeText(this@LoginActivity,"Вы авторизованы!",Toast.LENGTH_LONG).show()
                    }
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