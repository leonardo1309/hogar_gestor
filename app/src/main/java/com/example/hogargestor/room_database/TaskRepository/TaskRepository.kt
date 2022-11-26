package com.example.hogargestor.room_database.TaskRepository

import com.example.hogargestor.room_database.Task
import com.example.hogargestor.room_database.TaskDaO

class TaskRepository (private val taskdao : TaskDaO) {
    suspend fun getAllTasks() : List<Task>{
        return taskdao.getAllTasks()
    }
    suspend fun insertTask(task: Task) : Long{
        return taskdao.insertTask(task)
    }
    suspend fun insertTasks(tasklist : List<Task>): List<Long> {
        return taskdao.insertTasks(tasklist)
    }
}