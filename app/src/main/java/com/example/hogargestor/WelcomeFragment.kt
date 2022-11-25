package com.example.hogargestor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_welcome, container, false)
        val btnTasks = fragment.findViewById<Button>(R.id.btnTasks)
        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.welcomeToolbar)
        toolbar?.isVisible = true
        btnTasks?.setOnClickListener{onViewTasks()}
        return fragment
    }


    private fun onViewTasks() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.fcv, TaskFragment::class.java,null,"tasks")
            ?.addToBackStack("")?.commit()
    }
}