package com.example.android_frontend_tennis.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.android_frontend_tennis.R

class ProgressButtonSVG(ct: Context, view: View) {
    private lateinit var cardView: CardView
    private lateinit var svg:ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var view:View
    init {
        cardView = view.findViewById(R.id.PBSvg_cardView)
        svg = view.findViewById(R.id.PBSvg_ivIcon)
        progressBar = view.findViewById(R.id.PBSvg_progressBar)

        this.view = view;
    }

    fun buttonActivated(){
        progressBar.visibility =View.VISIBLE
        svg.visibility = View.GONE
        view.isEnabled = false;
        view.isClickable =false;
    }

    fun buttonFinished(){
        view.isEnabled = true;
        view.isClickable =true;
        progressBar.visibility =View.GONE
        svg.visibility = View.VISIBLE
    }
}