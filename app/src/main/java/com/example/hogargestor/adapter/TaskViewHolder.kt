package com.example.hogargestor.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hogargestor.DashboardActivity
import com.example.hogargestor.R
import com.example.hogargestor.Task

class TaskViewHolder(view:View) : ViewHolder(view) {

    private val taskname = view.findViewById<TextView>(R.id.taskName)
    private val done = view.findViewById<CheckBox>(R.id.taskDone)

    fun render(task: Task, onClickListener:(Task) -> Unit){
        taskname.text = task.name
        done.isChecked = task.done
        itemView.setOnClickListener {
            onClickListener(task)
        }
    }
}