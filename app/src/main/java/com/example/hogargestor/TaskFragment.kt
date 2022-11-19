package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hogargestor.adapter.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class TaskFragment : Fragment(), DashboardActivity.SetTask {

    val adapter1 = TaskAdapter(TaskProvider.taskList, { onItemSelected(it) }, {onItemLongPressed(it)})
    private var newTaskName: EditText? = null
    private var newTaskPlace: EditText? = null
    private var newTaskTime: TimePicker? = null
    private var fab: FloatingActionButton? = null

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
        val adapter = recycler.adapter
        fab = fragmentTask.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            onAddTask(
                LayoutInflater.from(fragmentTask.context).inflate(R.layout.dialog_add, null), "add"
            )
        }
        toolbar?.title = getString(R.string.taskToolbar)
        toolbar?.isVisible = true
        initRecyclerView(recycler)
        return fragmentTask
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        val manager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter1

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem: Task = TaskProvider.taskList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                TaskProvider.taskList.removeAt(position)
                adapter1.notifyItemRemoved(position)

                Snackbar.make(recyclerView, "Deleted " + deletedItem.name, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            TaskProvider.taskList.add(position, deletedItem)
                            adapter1.notifyItemInserted(position)
                        }).show()
            }

        }).attachToRecyclerView(recyclerView)

    }

    private fun onItemSelected(task: Task){
        val data =Bundle()
        data.putInt("index", TaskProvider.taskList.indexOf(task))
        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, DetailFragment::class.java,data,"detail")
            ?.addToBackStack("")?.commit()
    }

    private fun onItemLongPressed(task: Task): Boolean {
        val position = TaskProvider.taskList.indexOf(task)
        onAddTask(LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_add, null),
            "edit", task, position)
        return true
    }

    private fun onAddTask(view: View, type: String, task: Task ?= null, position: Int ?= null) {
        val btnAdd: Button = view.findViewById(R.id.add_button)
        val btnCancel = view.findViewById<Button>(R.id.cancel_button)
        var title: String = "default"
        newTaskName = view.findViewById<EditText>(R.id.newTaskName)
        newTaskTime = view.findViewById<TimePicker>(R.id.timePicker)
        newTaskPlace = view.findViewById<EditText>(R.id.newTaskPlace)

        if(type == "add"){
            btnAdd.text = getString(R.string.add)
            title = getString(R.string.addTask)
        } else if(type == "edit") {
            title = getString(R.string.editTask)
            btnAdd.text = getString(R.string.edit)
            newTaskName?.setText(task?.name)
            val hour = task?.time?.substring(0,2)?.toInt()
            val minute = task?.time?.substring(3,5)?.toInt()
            newTaskTime?.hour = hour!!
            newTaskTime?.minute = minute!!
            newTaskPlace?.setText(task.place)
        }
        val builder = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle(title)
        val showCustomDialog = builder.show()
        btnCancel.setOnClickListener { showCustomDialog.dismiss() }
        if(type == "add"){
            btnAdd.setOnClickListener {
                setTask()
                showCustomDialog.dismiss()
            }
        }else if (type == "edit"){
            btnAdd.setOnClickListener {
                editTask(task!!, position!!)
                showCustomDialog.dismiss()
            }
        }

    }

    private fun editTask(task: Task, position: Int?) {
        task.name = newTaskName?.text.toString()
        task.place = newTaskPlace?.text.toString()
        var hour = newTaskTime?.hour.toString()
        if(hour.toInt() < 10) hour = "0$hour"
        var minute = newTaskTime?.minute.toString()
        if(minute.toInt() < 10) minute = "0$minute"
        task.time = "$hour:$minute"

        adapter1.notifyItemChanged(position!!)
    }

    private fun setTask() {
        val name = newTaskName?.text.toString()
        val place = newTaskPlace?.text.toString()
        var hour = newTaskTime?.hour.toString()
        if(hour.toInt() < 10) hour = "0$hour"
        var minute = newTaskTime?.minute.toString()
        if(minute.toInt() < 10) minute = "0$minute"
        val newTask = Task(name, "$hour:$minute", place, false)
        TaskProvider.taskList.add(newTask)
        adapter1.notifyItemInserted(TaskProvider.taskList.lastIndex)
    }
}