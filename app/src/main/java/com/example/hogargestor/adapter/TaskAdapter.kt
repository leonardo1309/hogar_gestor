package com.example.hogargestor.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hogargestor.DashboardActivity
import com.example.hogargestor.R
import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskAdapter(context: Context, private val onClickListener:(Task) -> Unit, private val onLongClickListener:(Task) -> Boolean) : RecyclerView.Adapter<TaskViewHolder>() {

    val db = TaskProvider.getDatabase(context)
    val taskDao = db.taskDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var item = Task(0, "","","",false)
        runBlocking {
            launch {
                if (taskDao.getAllTasks().isNotEmpty()) {
                    item = taskDao.getAllTasks()[position]
                }
            }
        }
        holder.render(item, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int{
        var size = 0
        runBlocking {
            launch {
                size = taskDao.getAllTasks().size
            }
        }
        return size
    }
}