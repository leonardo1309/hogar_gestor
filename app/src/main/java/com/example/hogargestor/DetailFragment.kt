package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_detail, container, false)
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.isVisible = false
        var index = requireArguments().getInt("index")
        var tvTaskName: TextView = fragment.findViewById(R.id.tvTaskName)
        var tvTaskTime: TextView = fragment.findViewById(R.id.tvTaskTime)
        var tvTaskPlace: TextView = fragment.findViewById(R.id.tvTaskPlace)
        val db = TaskProvider.getDatabase(tvTaskName.context)
        val taskDao = db.taskDao()
        var task = Task(0,"","","", false)
        runBlocking {
            launch {
                task = taskDao.getAllTasks()[index]
            }
        }

        tvTaskName.text = task.name
        tvTaskTime.text = task.time
        tvTaskPlace.text = task.place

        return fragment
    }
}