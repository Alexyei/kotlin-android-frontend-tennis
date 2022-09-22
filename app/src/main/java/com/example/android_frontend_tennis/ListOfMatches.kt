package com.example.android_frontend_tennis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListOfMatches : AppCompatActivity() {
    private lateinit var btnNewMatch: View;
    private lateinit var rvMatches:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_matches)

        btnNewMatch = findViewById(R.id.btnNewMatch)
        rvMatches = findViewById(R.id.rvMatches)
    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}