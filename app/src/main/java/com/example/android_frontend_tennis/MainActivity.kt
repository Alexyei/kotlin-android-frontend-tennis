package com.example.android_frontend_tennis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
//    private lateinit var tfHello:TextView
//    private lateinit var btn:Button
//    private lateinit var loginButton:View;
    private var isLoading:Boolean = false;
    private lateinit var pbMain:ProgressBar

//    private val service = PostsService.create()
//    private var posts = emptyList<PostResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pbMain = findViewById(R.id.pbMain)

        pbMain.visibility = View.VISIBLE;
        val handler = Handler()
            handler.postDelayed(Runnable {
                pbMain.visibility = View.GONE
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            },3000)

//        tfHello = findViewById(R.id.tfHello)
//        btn = findViewById(R.id.button)
//        btn.setOnClickListener(this)
//        loginButton = findViewById(R.id.LoginBtn)
//        tfHello.text = isLoading.toString()
//        val progressButton = ProgressButton(this@MainActivity,loginButton,"Войти")
//
//        loginButton.setOnClickListener(View.OnClickListener { v->
//            progressButton.buttonActivated()
//            val handler = Handler()
//            handler.postDelayed(Runnable { progressButton.buttonFinished() },3000)
//        })

        lifecycleScope.launchWhenCreated {



//        getPosts()


        }
    }

    suspend fun getPosts(){
//        coroutineScope {
//            launch {
                isLoading = true;
//                tfHello.text = isLoading.toString()
        delay(2000)
             //   service.getPosts();
                isLoading = false;
//            }}
    }

    override fun onClick(p0: View?) {
        isLoading = !isLoading;
//        tfHello.text = isLoading.toString()
    }


}


