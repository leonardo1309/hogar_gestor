package com.example.hogargestor.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hogargestor.R
import com.example.hogargestor.room_database.Task

class TaskViewHolder(view:View) : ViewHolder(view) {

    private val taskname = view.findViewById<TextView>(R.id.taskName)
    private val done = view.findViewById<CheckBox>(R.id.taskDone)

    fun render(task: Task, onClickListener: (Task) -> Unit, onLongClickListener: (Task) -> Boolean){
        taskname.text = task.name
        done.isChecked = task.done
        itemView.setOnClickListener {
            onClickListener(task)
        }
        itemView.setOnLongClickListener{
            onLongClickListener(task)
        }
    }
}