package com.example.android_frontend_tennis

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android_frontend_tennis.ui.components.ProgressButton
import java.text.SimpleDateFormat
import java.util.*

class MatchCardAdapter(
    private val matches: MutableList<MatchCard>
) : RecyclerView.Adapter<MatchCardAdapter.MatchCardViewHolder>() {

    class MatchCardViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView)
    private lateinit var context: Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchCardViewHolder {
        val holder = MatchCardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.match_card,
                parent,
                false
            )
        )

        context = parent.context;
        val delete_btn = holder.itemView.findViewById<View>(R.id.btnMatchCardDelete)

        ProgressButton(parent.context,delete_btn,"Удалить")


        return holder;
    }

    fun addMatch(match: MatchCard) {
        matches.add(match)
        notifyItemInserted(matches.size - 1)
    }

//    fun deleteDoneTodos() {
//        todos.removeAll { todo ->
//            todo.isChecked
//        }
//        notifyDataSetChanged()
//    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: MatchCardViewHolder, position: Int) {
        val curMatch = matches[position]
        holder.itemView.apply {


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

            this.findViewById<View>(R.id.btnMatchCardDelete).setOnClickListener(View.OnClickListener { v->
                Toast.makeText(context,"DELETE CLICK",Toast.LENGTH_LONG).show()
            })

            this.findViewById<View>(R.id.btnMatchCardSave).setOnClickListener(View.OnClickListener { v->
                Toast.makeText(context,"SAVE CLICK",Toast.LENGTH_LONG).show()
            })

//            tvTodoTitle.text = curTodo.title
//            cbDone.isChecked = curTodo.isChecked
//            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
//            cbDone.setOnCheckedChangeListener { _, isChecked ->
//                toggleStrikeThrough(tvTodoTitle, isChecked)
//                curTodo.isChecked = !curTodo.isChecked
//            }
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    fun getItem(position: Int):MatchCard{

        return matches[position]
    }
}