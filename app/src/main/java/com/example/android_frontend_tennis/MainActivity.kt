package com.example.android_frontend_tennis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.android_frontend_tennis.api.ServiceManager
import com.example.android_frontend_tennis.api.auth.AuthResult
import com.example.android_frontend_tennis.api.auth.AuthService
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay


class MainActivity : AppCompatActivity(){
//    private lateinit var tfHello:TextView
//    private lateinit var btn:Button
//    private lateinit var loginButton:View;
    private var isLoading:Boolean = false;
    private lateinit var pbMain:ProgressBar

    private lateinit var authService:AuthService;


//    private val service = PostsService.create()
//    private var posts = emptyList<PostResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authService = ServiceManager.getAuthService(getPreferences(MODE_PRIVATE))
        pbMain = findViewById(R.id.pbMain)

        pbMain.visibility = View.VISIBLE;

        val intent = Intent(this, ListOfMatches::class.java)
        startActivity(intent)


//        val handler = Handler()
//            handler.postDelayed(Runnable {
//                pbMain.visibility = View.GONE
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//
//            },3000)

//        lifecycleScope.launchWhenCreated {
//            while (true){
//                var result = authService.authenticate()
//                when(result){
//                    is AuthResult.Authorized ->{
//
//                        pbMain.visibility = View.GONE
//                        break;
//                    }
//                    is AuthResult.Unauthorized -> {
//                        pbMain.visibility = View.GONE
//                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                        break;
//                    }
//                    is AuthResult.UnknownError ->{
//                        Toast.makeText(this@MainActivity,"Проверьте интернет-соединение",Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
    }


}


