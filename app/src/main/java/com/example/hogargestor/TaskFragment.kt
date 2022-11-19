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

    val adapter1 = TaskAdapter(TaskProvider.taskList) { onItemSelected(it) }
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
                LayoutInflater.from(fragmentTask.context).inflate(R.layout.dialog_add, null)
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

    fun onItemSelected(task: Task){
        val data =Bundle()
        data.putInt("index", TaskProvider.taskList.indexOf(task))
        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, DetailFragment::class.java,data,"detail")
            ?.addToBackStack("")?.commit()
        Toast.makeText(activity,TaskProvider.taskList.size.toString(), Toast.LENGTH_LONG).show()
    }

    private fun onAddTask(view: View) {
        val builder = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle(getString(R.string.addTask))
        val showCustomDialog = builder.show()
        val cancel = view.findViewById<Button>(R.id.cancel_button)
        val add = view.findViewById<Button>(R.id.add_button)
        newTaskName = view.findViewById<EditText>(R.id.newTaskName)
        newTaskTime = view.findViewById<TimePicker>(R.id.timePicker)
        newTaskPlace = view.findViewById<EditText>(R.id.newTaskPlace)
        cancel.setOnClickListener { showCustomDialog.dismiss() }
        add.setOnClickListener {
            setTask()
            showCustomDialog.dismiss()
        }
    }

    private fun setTask() {
        val name = newTaskName?.text.toString()
        val place = newTaskPlace?.text.toString()
        val hour = newTaskTime?.hour.toString()
        val minute = newTaskTime?.minute.toString()
        val newTask = Task(name, "$hour:$minute", place, false)
        TaskProvider.taskList.add(newTask)
        adapter1.notifyItemInserted(TaskProvider.taskList.lastIndex)
    }

}