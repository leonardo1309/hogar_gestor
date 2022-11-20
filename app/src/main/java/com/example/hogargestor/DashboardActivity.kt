package com.example.hogargestor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hogargestor.room_database.Task

class DashboardActivity : AppCompatActivity() {

    private val sett: SetTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fcv, WelcomeFragment::class.java, null, "tasks")
                .commit()
        }
    }





    interface SetTask {
        fun updateList(task: Task) {}
    }
}