package com.example.android_frontend_tennis

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android_frontend_tennis.api.ServiceManager
import com.example.android_frontend_tennis.api.match.MatchService
import com.example.android_frontend_tennis.ui.components.ProgressButtonSVG
import kotlinx.datetime.toJavaInstant
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class MatchCardAdapter(
    private val matches: List<MatchCard>,
    private val onCallback:IMatchCardCB,
    private val pref:SharedPreferences
) : RecyclerView.Adapter<MatchCardAdapter.MatchCardViewHolder>() {

    class MatchCardViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView)
//    private lateinit var context: Context;
    private lateinit var deleteBtn:ProgressButtonSVG;
    private lateinit var saveBtn:ProgressButtonSVG;
    private lateinit var matchService: MatchService;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchCardViewHolder {
        val holder = MatchCardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.match_card,
                parent,
                false
            )
        )
//
//        context = parent.context;


        return holder;
    }



    override fun onBindViewHolder(holder: MatchCardViewHolder, position: Int) {
        val curMatch = matches[position]
        holder.itemView.apply {


            val delete_btn = holder.itemView.findViewById<View>(R.id.btnMatchCardDelete)
            val save_btn = holder.itemView.findViewById<View>(R.id.btnMatchCardSave)


            deleteBtn = ProgressButtonSVG(context, delete_btn )
            saveBtn = ProgressButtonSVG(context, save_btn )

            matchService = ServiceManager.getMatchService(pref)


//            val sdf = SimpleDateFormat("dd.MM.uuuu HH:mm", Locale.getDefault())
            val formatter: DateTimeFormatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());

            formatter.format(curMatch.created.toJavaInstant())
            Log.e("BIND","BIND CARD")

            this.findViewById<TextView>(R.id.tvMatchCardStartedAt).text = formatter.format(curMatch.created.toJavaInstant())
            this.findViewById<TextView>(R.id.tvFirstPlayerName).text = curMatch.firstPlayerName.toString()
            this.findViewById<TextView>(R.id.tvSecondPlayerName).text = curMatch.secondPlayerName.toString()
            this.findViewById<TextView>(R.id.tvFirstPlayerPoints).text = curMatch.points.first.toString()
            this.findViewById<TextView>(R.id.tvSecondPlayerPoints).text = curMatch.points.second.toString()
            this.findViewById<TextView>(R.id.tvFirstPlayerSets).text =  curMatch.sets.first.joinToString(" ")
            this.findViewById<TextView>(R.id.tvSecondPlayerSets).text =  curMatch.sets.second.joinToString(" ")



            this.setOnClickListener(View.OnClickListener { v->
                val intent = Intent(context, Match::class.java)
                intent.putExtra("position", position);
                startActivity(context,intent,null)
            })

            fun blockCard(){
                this.isClickable = false;
                this.isEnabled = false;

                save_btn.isClickable = false;
                save_btn.isEnabled = false;

                delete_btn.isEnabled = false;
                delete_btn.isClickable = false;
            }

            fun unBlockCard(){
                this.isClickable = true;
                this.isEnabled = true;

                save_btn.isClickable = true;
                save_btn.isEnabled = true;

                delete_btn.isEnabled = true;
                delete_btn.isClickable = true;
            }

            fun displaySaveBtn(saved:Boolean){
                if (saved){
                    save_btn.visibility = View.GONE
                }else{
                    save_btn.visibility = View.VISIBLE
                }
            }

            this.findViewById<View>(R.id.btnMatchCardDelete).setOnClickListener(View.OnClickListener { v->

                blockCard()
                deleteBtn.buttonActivated()

                onCallback.onDeleteCallback(matchService,curMatch) {
                    DataObject.deleteData(position)
                    notifyItemChanged(position)
                    deleteBtn.buttonFinished()
                    unBlockCard()
                }
//                Toast.makeText(context,"DELETE CLICK",Toast.LENGTH_LONG).show()
//                Handler(Looper.getMainLooper()).postDelayed({
//
//                    DataObject.deleteData(position)
//                    notifyItemChanged(position)
//                    deleteBtn.buttonFinished()
//                    unBlockCard()
//                },2000)


            })

            this.findViewById<View>(R.id.btnMatchCardSave).setOnClickListener(View.OnClickListener { v->
                blockCard()
                saveBtn.buttonActivated()
                onCallback.onSaveCallback(matchService,curMatch) { it->
                    curMatch.saved = it;
                    notifyItemChanged(position)
                    saveBtn.buttonFinished()
                    unBlockCard()
                }
            })

            displaySaveBtn(curMatch.saved)
        }

    }


    override fun getItemCount(): Int {
        return matches.size
    }

}