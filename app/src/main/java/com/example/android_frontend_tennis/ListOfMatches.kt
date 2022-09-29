package com.example.android_frontend_tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_frontend_tennis.api.match.MatchResult
import com.example.android_frontend_tennis.api.match.MatchService
import com.example.android_frontend_tennis.ui.components.ProgressButton
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
interface IOnSaveCB{
    fun onSaveCallback(matchService: MatchService, curMatch:MatchCard, cb:()->Unit)
}
class ListOfMatches : AppCompatActivity() {
    private lateinit var btnNewMatch: View;
    private lateinit var rvMatches:RecyclerView
    private lateinit var matchCardAdapter: MatchCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_matches)

        btnNewMatch = findViewById(R.id.btnNewMatch)
        rvMatches = findViewById(R.id.rvMatches)

        ProgressButton(this,btnNewMatch,"Создать матч")

//        Log.e("Init","list init")
//        matchCardAdapter = MatchCardAdapter(mutableListOf())
//        matchCardAdapter = GlobalVariables().matchCardAdapter as MatchCardAdapter
//        matchCardAdapter = GlobalVariablesInstance.getMatchCardAdapterInstance()
//        rvMatches.adapter =  matchCardAdapter
//        rvMatches.layoutManager = LinearLayoutManager(this)

        btnNewMatch.setOnClickListener(View.OnClickListener { v->
//            val match = MatchCard(null, Calendar.getInstance().getTime(),false,Math.random().toString(),"s player",ArrayList<Int>(),ArrayList<Int>(),"15","40")
//            matchCardAdapter.addMatch(match)

//            Toast.makeText(this,matchCardAdapter.itemCount.toString(),Toast.LENGTH_LONG).show()

            val intent = Intent(this, NewMatch::class.java)
            startActivity(intent)
        })

        setRecycler()

    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }



//    fun onSaveCallback(matchService: MatchService, curMatch:MatchCard, cb:()->Unit){
//        lifecycleScope.launch {
//            var result = matchService.insertOrUpdate(curMatch)
//
//            when (result){
//                is MatchResult.InsertedSuccess ->{
//
//                    Toast.makeText(this@ListOfMatches,result.data.toString(),Toast.LENGTH_LONG).show()
//
//                }
//                is MatchResult.Unauthorized ->{
//
//                    Toast.makeText(this@ListOfMatches, "Вы не авторизованы", Toast.LENGTH_LONG)
//                        .show()
//                }
//                else->{
//
//                    Toast.makeText(this@ListOfMatches, "Непредвиденная ошибка", Toast.LENGTH_LONG)
//                        .show()
//
//                }
//
//            }
//    cb()
//        }
//
//    }

    fun setRecycler(){
        Log.e("Init","list init")
        rvMatches.adapter =  MatchCardAdapter(DataObject.getAllData(),object :IOnSaveCB{
            override fun onSaveCallback(
                matchService: MatchService,
                curMatch: MatchCard,
                cb: () -> Unit
            ) {
                lifecycleScope.launch {
                    var result = matchService.insertOrUpdate(curMatch)

                    when (result){
                        is MatchResult.Success ->{

                            Toast.makeText(this@ListOfMatches,result.data.toString(),Toast.LENGTH_LONG).show()

                        }
                        is MatchResult.Unauthorized ->{

                            Toast.makeText(this@ListOfMatches, "Вы не авторизованы", Toast.LENGTH_LONG)
                                .show()
                        }
                        else->{

                            Toast.makeText(this@ListOfMatches, "Непредвиденная ошибка", Toast.LENGTH_LONG)
                                .show()

                        }

                    }
                    cb()
                }
            }

        })
        rvMatches.layoutManager = LinearLayoutManager(this)
    }
}