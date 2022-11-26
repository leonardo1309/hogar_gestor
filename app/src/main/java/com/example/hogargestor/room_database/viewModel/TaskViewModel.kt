package com.example.hogargestor.room_database.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskRepository.TaskRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {
    var tasks: List<Task>? = null


    fun getAllTasks(): Job{
        return viewModelScope.async {
            tasks = repository.getAllTasks()
        }
    }

    fun getTheTasks(): List<Task>? {
        return tasks
    }

    fun insertTask(task: Task): Long{
        var idTask: Long = 0
        viewModelScope.launch {
            idTask = repository.insertTask(task)
        }
        return idTask
    }
    fun insertTasks(tasks: List<Task>): List<Long>? {
        var idTask : List<Long>? = null
        viewModelScope.launch {
            idTask = repository.insertTasks(tasks)
        }
        return idTask
    }
}