package com.example.android_frontend_tennis.ui.components

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.android_frontend_tennis.R

class ProgressButton(ct: Context, view: View, text:String = "Loading") {
    private lateinit var cardView: CardView
    private lateinit var constraintLayout: ConstraintLayout
//    private lateinit var mainLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView


    private lateinit var animation: Animation

    private lateinit var text1:String
    private lateinit var view1:View
    init {
        cardView = view.findViewById(R.id.PB_cardView)
        constraintLayout = view.findViewById(R.id.PB_constLayout)
        progressBar = view.findViewById(R.id.PB_progressBar)
        textView = view.findViewById(R.id.PB_textView)
//        mainLayout = view.findViewById(R.id.PB_main)

        textView.text = text
        this.text1 = text;
        this.view1 = view;
    }

    fun buttonActivated(){
        progressBar.visibility =View.VISIBLE
//        mainLayout.isEnabled = false;
//        mainLayout.isClickable = false;
        view1.isEnabled = false;
        view1.isClickable =false;
        textView.text = ""
    }

    fun buttonFinished(){
        textView.text = this.text1;
        view1.isEnabled = true;
        view1.isClickable =true;
        progressBar.visibility =View.GONE
    }
}