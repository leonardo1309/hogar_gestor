package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val task = TaskProvider.taskList[index]

        tvTaskName.text = task.name
        tvTaskTime.text = task.time
        tvTaskPlace.text = task.place

        return fragment
    }
}