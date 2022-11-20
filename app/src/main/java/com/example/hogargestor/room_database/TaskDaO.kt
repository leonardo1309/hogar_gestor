package com.example.hogargestor.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDaO {
    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task) : Long
    @Update
    suspend fun updateTask(task: Task)
    @Delete
    suspend fun deleteTask(task: Task)
}