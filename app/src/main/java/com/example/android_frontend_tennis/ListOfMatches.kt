package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_frontend_tennis.api.ServiceManager
import com.example.android_frontend_tennis.api.match.MatchResponse
import com.example.android_frontend_tennis.api.match.MatchResult
import com.example.android_frontend_tennis.api.match.MatchService
import com.example.android_frontend_tennis.ui.components.ProgressButton
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.util.*
import kotlin.collections.ArrayList

interface IMatchCardCB {
    fun onSaveCallback(matchService: MatchService, curMatch: MatchCard, cb: () -> Unit)
    fun onDeleteCallback(matchService: MatchService, curMatch: MatchCard, cb: () -> Unit)
}

class ListOfMatches : AppCompatActivity() {
    private lateinit var btnNewMatch: View;
    private lateinit var rvMatches: RecyclerView
    private lateinit var matchCardAdapter: MatchCardAdapter
    private lateinit var loadScreen: ConstraintLayout
    private lateinit var matchService: MatchService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_matches)

        btnNewMatch = findViewById(R.id.btnNewMatch)
        rvMatches = findViewById(R.id.rvMatches)
        loadScreen = findViewById(R.id.loadScreen)

        matchService = ServiceManager.getMatchService(getSharedPreferences("token", MODE_PRIVATE))


        ProgressButton(this, btnNewMatch, "Создать матч")


        btnNewMatch.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(this, NewMatch::class.java)
            startActivity(intent)
        })


        if (!DataObject.isInit) {
            lifecycleScope.launchWhenCreated {
                var result = matchService.getMyMatches()
                when (result) {
                    is MatchResult.WithError -> {
                        Toast.makeText(this@ListOfMatches, "Ошибка подключения", Toast.LENGTH_LONG)
                            .show()

                    }
                    is MatchResult.Unauthorized -> {
                        Toast.makeText(this@ListOfMatches, "Вы не авторизованы", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@ListOfMatches, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is MatchResult.Success -> {
                        Log.e("res", (result.data as ArrayList<MatchResponse>).count().toString())
                        val cards = (result.data as ArrayList<MatchResponse>).map { el ->
                            MatchCard(
                                el.id,
                                Instant.parse(el.created),
                                el.setCount,
                                el.endType,
                                el.whoServiceFirst,
                                true,
                                el.firstPlayerName,
                                el.secondPlayerName,
                                el.penalties,
                                el.sets,
                                el.points
                            )
                        }

                        DataObject.listdata = cards.toMutableList();
                    }
                }
                DataObject.isInit = true;
                loadScreen.visibility = View.GONE
                setRecycler()
            }
        } else {
            setRecycler()
        }


    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }


    fun setRecycler() {
        Log.e("Init", "list init")
        rvMatches.adapter = MatchCardAdapter(DataObject.getAllData(), object : IMatchCardCB {
            override fun onSaveCallback(
                matchService: MatchService,
                curMatch: MatchCard,
                cb: () -> Unit
            ) {
                lifecycleScope.launch {
                    var result = matchService.insertOrUpdate(curMatch)

                    when (result) {
                        is MatchResult.Success -> {

                            Toast.makeText(this@ListOfMatches, "Сохранено", Toast.LENGTH_LONG)
                                .show()

                        }
                        is MatchResult.Unauthorized -> {

                            Toast.makeText(
                                this@ListOfMatches,
                                "Вы не авторизованы",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@ListOfMatches, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {

                            Toast.makeText(
                                this@ListOfMatches,
                                "Непредвиденная ошибка",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }

                    }
                    cb()
                }
            }

            override fun onDeleteCallback(
                matchService: MatchService,
                curMatch: MatchCard,
                cb: () -> Unit
            ) {
                lifecycleScope.launch {
                    if (curMatch.id != null) {
                        var result = matchService.deleteMatch(curMatch.id!!)

                        when (result) {
                            is MatchResult.Success -> {

                                Toast.makeText(this@ListOfMatches, "Удалено", Toast.LENGTH_LONG)
                                    .show()

                            }
                            is MatchResult.Unauthorized -> {

                                Toast.makeText(
                                    this@ListOfMatches,
                                    "Вы не авторизованы",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@ListOfMatches, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            else -> {

                                Toast.makeText(
                                    this@ListOfMatches,
                                    "Непредвиденная ошибка",
                                    Toast.LENGTH_LONG
                                )
                                    .show()

                            }

                        }
                    }
                    cb()
                }
            }

        }, getSharedPreferences("token", MODE_PRIVATE))
        rvMatches.layoutManager = LinearLayoutManager(this)
    }
}