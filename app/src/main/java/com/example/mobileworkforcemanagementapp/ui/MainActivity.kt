package com.example.mobileworkforcemanagementapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mobileworkforcemanagementapp.MyApplication
import com.example.mobileworkforcemanagementapp.R
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var todoItemViewModel: TodoItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.get(this).getApplicationComponent().inject(this)
        supportActionBar?.hide()
        todoItemViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
    }
}