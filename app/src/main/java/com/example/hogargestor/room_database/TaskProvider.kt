package com.example.hogargestor.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskProvider: RoomDatabase() {
    abstract fun taskDao() : TaskDaO
    companion object{
        @Volatile
        private var taskList : TaskProvider?= null
        fun getDatabase(context: Context): TaskProvider {
            return taskList ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    TaskProvider::class.java, "TaskDatabase").build()
                taskList = instance
                instance
            }
        }
    }
}