package com.example.hogargestor

class TaskProvider {
    companion object{
        val taskList = listOf<Task>(
            Task("Develop","now","here", true),
            Task("watch","then", "bed", false),
            Task("eat","morning", "table",false),
            Task("work","tomorrow", "aeromar", true),
            Task("swim","afternoon", "beach", false),
            Task("sleep","night", "bedroom", true),

        )
    }
}