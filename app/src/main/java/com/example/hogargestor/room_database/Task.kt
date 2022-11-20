package com.example.hogargestor.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (@PrimaryKey(autoGenerate = true)val id: Int, var name: String, var time: String, var place: String, var done: Boolean)