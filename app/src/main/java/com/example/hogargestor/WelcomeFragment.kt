package com.example.hogargestor

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WelcomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_welcome, container, false)
        val btnTasks = fragment.findViewById<Button>(R.id.btnTasks)
        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.welcomeToolbar)
        toolbar?.isVisible = true
        btnTasks?.setOnClickListener{onClickListener(requireActivity())}
        return fragment
    }


    fun onClickListener(context: Context){
        val db = TaskProvider.getDatabase(context)
        val taskDao = db.taskDao()
        runBlocking {
            launch {
                taskDao.insertTask(Task(0,"default","13:20","Here", false))
            }
        }
        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, TaskFragment::class.java,null,"tasks")
            ?.addToBackStack("")?.commit()
    }
}