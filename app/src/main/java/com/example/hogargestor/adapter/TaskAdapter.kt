package com.example.hogargestor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hogargestor.R
import com.example.hogargestor.Task

class TaskAdapter(private val taskList: List<Task>, private val onClickListener:(Task) -> Unit, private val onLongClickListener:(Task) -> Boolean) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = taskList[position]
        holder.render(item, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int = taskList.size
}