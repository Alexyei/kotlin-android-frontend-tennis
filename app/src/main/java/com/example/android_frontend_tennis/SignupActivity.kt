package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.android_frontend_tennis.ui.components.ProgressButton

class SignupActivity : AppCompatActivity() {
    private lateinit var signupButton: View;
    private lateinit var loginLink: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        signupButton = findViewById(R.id.signupBtn)
        loginLink = findViewById(R.id.tvSignupLoginLink)

        loginLink.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        val progressButton = ProgressButton(this@SignupActivity,signupButton,"Создать аккаунт")
    }
}