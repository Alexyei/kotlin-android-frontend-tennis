package com.example.android_frontend_tennis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.android_frontend_tennis.api.ServiceManager
import com.example.android_frontend_tennis.api.auth.AuthResult
import com.example.android_frontend_tennis.api.auth.AuthService
import com.example.android_frontend_tennis.api.match.MatchResponse
import com.example.android_frontend_tennis.api.match.MatchResult
import com.example.android_frontend_tennis.api.match.MatchService
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant


class MainActivity : AppCompatActivity(){
//    private lateinit var tfHello:TextView
//    private lateinit var btn:Button
//    private lateinit var loginButton:View;
    private var isLoading:Boolean = false;
    private lateinit var pbMain:ProgressBar

    private lateinit var authService:AuthService;
    private lateinit var matchService:MatchService;


//    private val service = PostsService.create()
//    private var posts = emptyList<PostResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        получаем jwt-токен из файла настроек и создаём authService
//        token - это название файла настроек
        authService = ServiceManager.getAuthService(getSharedPreferences("token",MODE_PRIVATE))
        //        получаем jwt-токен из файла настроек и создаём matchService
        matchService = ServiceManager.getMatchService(getSharedPreferences("token",MODE_PRIVATE))
        pbMain = findViewById(R.id.pbMain)

        pbMain.visibility = View.VISIBLE;







//        val handler = Handler()
//            handler.postDelayed(Runnable {
//                pbMain.visibility = View.GONE
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//
//            },3000)

//        если мы успшено авторизовались переходим к списку наших матчей
//        если нет (токен просрочен или неверен) переходим к форме входа
//        если не можем связаться с сервером, выводим сообщение: проверьте интернет-соединение
        lifecycleScope.launchWhenCreated {
            while (true){
                var result = authService.authenticate()
                when(result){
                    is AuthResult.Authorized ->{
                        val intent = Intent(this@MainActivity, ListOfMatches::class.java)
                        startActivity(intent)
                        pbMain.visibility = View.GONE
                        break;
                    }
                    is AuthResult.Unauthorized -> {
                        pbMain.visibility = View.GONE
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        break;
                    }
                    is AuthResult.UnknownError ->{
                        Toast.makeText(this@MainActivity,"Проверьте интернет-соединение",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}


