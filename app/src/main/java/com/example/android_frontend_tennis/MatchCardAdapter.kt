package com.example.android_frontend_tennis

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android_frontend_tennis.ui.components.ProgressButton
import com.example.android_frontend_tennis.ui.components.ProgressButtonSVG
import java.text.SimpleDateFormat
import java.util.*

class MatchCardAdapter(
    private val matches: List<MatchCard>
) : RecyclerView.Adapter<MatchCardAdapter.MatchCardViewHolder>() {

    class MatchCardViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView)
//    private lateinit var context: Context;
    private lateinit var deleteBtn:ProgressButtonSVG;
    private lateinit var saveBtn:ProgressButtonSVG;
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

//    fun addMatch(match: MatchCard) {
//        matches.add(match)
//        notifyItemInserted(matches.size - 1)
//    }

//    fun deleteDoneTodos() {
//        todos.removeAll { todo ->
//            todo.isChecked
//        }
//        notifyDataSetChanged()
//    }

//    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
//        if(isChecked) {
//            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        } else {
//            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//        }
//    }

    override fun onBindViewHolder(holder: MatchCardViewHolder, position: Int) {
        val curMatch = matches[position]
        holder.itemView.apply {


            val delete_btn = holder.itemView.findViewById<View>(R.id.btnMatchCardDelete)
            val save_btn = holder.itemView.findViewById<View>(R.id.btnMatchCardSave)


            //"@colors/text",context.getDrawable(R.color.background)
//            ProgressButton(context,delete_btn,"", context.getDrawable(R.drawable.ic_baseline_delete_outline_24), "white",R.color.background)
            deleteBtn = ProgressButtonSVG(context, delete_btn )
            saveBtn = ProgressButtonSVG(context, save_btn )

            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())


            this.findViewById<TextView>(R.id.tvMatchCardStartedAt).text = sdf.format(curMatch.created)
            this.findViewById<TextView>(R.id.tvFirstPlayerName).text = curMatch.firstPlayerName.toString()
            this.findViewById<TextView>(R.id.tvSecondPlayerName).text = curMatch.secondPlayerName.toString()
            this.findViewById<TextView>(R.id.tvFirstPlayerPoints).text = curMatch.firstPlayerPoints.toString()
            this.findViewById<TextView>(R.id.tvSecondPlayerPoints).text = curMatch.secondPlayerPoints.toString()



            this.setOnClickListener(View.OnClickListener { v->
                Toast.makeText(context,"CARD CLICK",Toast.LENGTH_LONG).show()
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
                Toast.makeText(context,"DELETE CLICK",Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({

                    DataObject.deleteData(position)
                    notifyItemChanged(position)
                    deleteBtn.buttonFinished()
                    unBlockCard()
                },2000)


//                setRecycler()
//                val intent = Intent(context, ListOfMatches::class.java)
//                startActivity(context,intent,null)
            })

            this.findViewById<View>(R.id.btnMatchCardSave).setOnClickListener(View.OnClickListener { v->
                blockCard()
                saveBtn.buttonActivated()
                Toast.makeText(context,"SAVE CLICK",Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({

                    DataObject.updateData(position, true)
                    notifyItemChanged(position)
                    saveBtn.buttonFinished()
                    unBlockCard()
                },2000)
            })

//            tvTodoTitle.text = curTodo.title
//            cbDone.isChecked = curTodo.isChecked
//            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
//            cbDone.setOnCheckedChangeListener { _, isChecked ->
//                toggleStrikeThrough(tvTodoTitle, isChecked)
//                curTodo.isChecked = !curTodo.isChecked
//            }
            displaySaveBtn(curMatch.saved)
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }

//    fun getItem(position: Int):MatchCard{
//
//        return matches[position]
//    }
}