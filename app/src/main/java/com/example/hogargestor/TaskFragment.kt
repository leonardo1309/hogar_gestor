package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hogargestor.adapter.TaskAdapter

class TaskFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentTask = inflater.inflate(R.layout.fragment_task, container, false)
        val recycler = fragmentTask.findViewById<RecyclerView>(R.id.recyclerTasks)
        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.taskToolbar)
        toolbar?.isVisible = true
        initRecyclerView(recycler)
        return fragmentTask
    }

    fun initRecyclerView(recyclerView: RecyclerView) {
        val manager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = manager
        recyclerView.adapter = TaskAdapter(TaskProvider.taskList) { onItemSelected(it) }
    }

    fun onItemSelected(task: Task){
        val data =Bundle()
        data.putInt("index", TaskProvider.taskList.indexOf(task))
        activity?.getSupportFragmentManager()?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, DetailFragment::class.java,data,"detail")
            ?.addToBackStack("")?.commit()
    }
}