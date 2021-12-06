package com.example.mobileworkforcemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileworkforcemanagementapp.database.DatabaseHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoItemViewModel::class.java) -> TodoItemViewModel(application) as T
            else ->  throw IllegalArgumentException("Unknown class name")
        }
    }
}