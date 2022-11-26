package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hogargestor.adapter.TaskAdapter
import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskRepository.TaskRepository
import com.example.hogargestor.room_database.viewModel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Arrays.toString

class TaskFragment : Fragment(){

    private lateinit var newTaskName: EditText
    private lateinit var newTaskPlace: EditText
    private lateinit var newTaskTime: TimePicker
    private var fab: FloatingActionButton? = null
    var adapter1 : TaskAdapter? = null

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRepository: TaskRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentTask = inflater.inflate(R.layout.fragment_task, container, false)
        val recycler = fragmentTask.findViewById<RecyclerView>(R.id.recyclerTasks)
        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        adapter1 = TaskAdapter(recycler.context,{ onItemSelected(it) }, {onItemLongPressed(it)})
        fab = fragmentTask.findViewById(R.id.fab)
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
                val db = com.example.hogargestor.room_database.TaskProvider.getDatabase(recyclerView.context)
                val taskDao = db.taskDao()
                val dbFirebase = FirebaseFirestore.getInstance()
                val position = viewHolder.adapterPosition
                var deletedItem = Task(0,"","","",false)
                runBlocking {
                    launch {
                        val task = taskDao.getAllTasks()[position]
                        deletedItem = task
                        taskDao.deleteTask(task)
                        dbFirebase.collection("Tasks").document(task.id.toString())
                            .delete()
                        adapter1?.notifyItemRemoved(position)
                    }
                }

                Snackbar.make(recyclerView, getString(R.string.deleted) + deletedItem.name, Snackbar.LENGTH_LONG)
                    .setAction(
                        getString(R.string.undo),
                        View.OnClickListener {
                            runBlocking {
                                launch {
                                    taskDao.insertTask(deletedItem)
                                    adapter1?.notifyItemInserted(position)
                                }
                            }
                        }).show()
            }

        }).attachToRecyclerView(recyclerView)

    }

    private fun onItemSelected(task: Task){
        val db = com.example.hogargestor.room_database.TaskProvider.getDatabase(fab!!.context)
        val taskDao = db.taskDao()
        val data =Bundle()
        var index = 0
        runBlocking{
            launch {
                index = taskDao.getAllTasks().indexOf(task)
            }
        }
        data.putInt("index", index)
        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, DetailFragment::class.java,data,"detail")
            ?.addToBackStack("")?.commit()
    }

    private fun onItemLongPressed(task: Task): Boolean {
        val db = com.example.hogargestor.room_database.TaskProvider.getDatabase(fab!!.context)
        val taskDao = db.taskDao()
        var position = 0
        runBlocking{
            launch {
                position = taskDao.getAllTasks().indexOf(task)
            }
        }
        onAddTask(LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_add, null),
            "edit", task, position)
        return true
    }

    private fun onAddTask(view: View, type: String, task: Task?= null, position: Int ?= null) {
        val btnAdd: Button = view.findViewById(R.id.add_button)
        val btnCancel = view.findViewById<Button>(R.id.cancel_button)
        var title = "default"
        newTaskName = view.findViewById(R.id.newTaskName)
        newTaskTime = view.findViewById(R.id.timePicker)
        newTaskPlace = view.findViewById(R.id.newTaskPlace)

        if(type == "add"){
            btnAdd.text = getString(R.string.add)
            title = getString(R.string.addTask)
        } else if(type == "edit") {
            title = getString(R.string.editTask)
            btnAdd.text = getString(R.string.edit)
            newTaskName.setText(task?.name)
            val hour = task?.time?.substring(0,2)?.toInt()
            val minute = task?.time?.substring(3,5)?.toInt()
            newTaskTime.hour = hour!!
            newTaskTime.minute = minute!!
            newTaskPlace.setText(task.place)
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
        val db = com.example.hogargestor.room_database.TaskProvider.getDatabase(newTaskName.context)
        val taskDao = db.taskDao()
        val dbFirebase = FirebaseFirestore.getInstance()
        task.name = newTaskName.text.toString()
        task.place = newTaskPlace.text.toString()
        var hour = newTaskTime.hour.toString()
        if(hour.toInt() < 10) hour = "0$hour"
        var minute = newTaskTime.minute.toString()
        if(minute.toInt() < 10) minute = "0$minute"
        task.time = "$hour:$minute"
        runBlocking{
            launch {
                taskDao.updateTask(task)
                dbFirebase.collection("Tasks").document(task.id.toString())
                    .set(hashMapOf(
                        "name" to task.name,
                        "time" to task.time,
                        "place" to task.place
                    ))
            }
        }
        adapter1?.notifyItemChanged(position!!)
    }

    private fun setTask() {
        val db = com.example.hogargestor.room_database.TaskProvider.getDatabase(newTaskName.context)
        val taskDao = db.taskDao()
        val dbFirebase = FirebaseFirestore.getInstance()
        var index = 0
        val name = newTaskName.text.toString()
        val place = newTaskPlace.text.toString()
        var hour = newTaskTime.hour.toString()
        if(hour.toInt() < 10) hour = "0$hour"
        var minute = newTaskTime.minute.toString()
        if(minute.toInt() < 10) minute = "0$minute"
        val newTask = Task(0, name, "$hour:$minute", place, false)
        taskRepository = TaskRepository(taskDao)
        taskViewModel = TaskViewModel(taskRepository)
        val result = taskViewModel.getAllTasks()
        result.invokeOnCompletion {
            val theTasks = taskViewModel.getTheTasks()
            if(theTasks!!.isNotEmpty()){
                index = theTasks.size
            }
        }
        runBlocking {
            launch {
                val res = taskDao.insertTask(newTask)
                dbFirebase.collection("Tasks").document(res.toString())
                    .set(hashMapOf(
                        "name" to newTask.name,
                        "time" to newTask.time,
                        "place" to newTask.place
                    ))
                adapter1?.notifyItemInserted(taskDao.getAllTasks().lastIndex)
            }
        }
    }
}