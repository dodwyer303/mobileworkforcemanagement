package com.example.mobileworkforcemanagementapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.example.mobileworkforcemanagementapp.MyApplication
import com.example.mobileworkforcemanagementapp.R
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewEventViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var todoItemViewModel: TodoItemViewModel
    private lateinit var viewEventViewModel: ViewEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.get(this).getApplicationComponent().inject(this)
        supportActionBar?.hide()
        todoItemViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
        viewEventViewModel = ViewModelProvider(this, viewModelFactory)[ViewEventViewModel::class.java]
        viewEventViewModel.getCloseSoftKeyboardEvents().observe(this, {
            hideSoftKeyboard()
        })
    }

    private fun hideSoftKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}