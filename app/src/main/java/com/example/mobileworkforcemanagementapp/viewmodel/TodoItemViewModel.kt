package com.example.mobileworkforcemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileworkforcemanagementapp.database.DatabaseHelper
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import javax.inject.Inject

class TodoItemViewModel constructor(val application: Application): ViewModel() {
    private var toDoItemsLiveData: MutableLiveData<List<ToDoItem>> = MutableLiveData()
    private var databaseHelper = DatabaseHelper(application)
    init {
        databaseHelper = DatabaseHelper(application)
        updateToDoItemsLiveDataFromDataBase()
    }

    fun addTodoItem(toDoItem: ToDoItem) {
        databaseHelper.addToDoItem(toDoItem)
        updateToDoItemsLiveDataFromDataBase()
    }

    fun updateToDoItem(toDoItem: ToDoItem) {
        databaseHelper.updateTodoItem(toDoItem)
        updateToDoItemsLiveDataFromDataBase()
    }

    fun deleteToDoItem(toDoItem: ToDoItem) {
        databaseHelper.deleteToDoItem(toDoItem)
        updateToDoItemsLiveDataFromDataBase()
    }

    private fun updateToDoItemsLiveDataFromDataBase() {
        var toDoItems = databaseHelper.getTodoItems()
        if (toDoItems != null) {
            toDoItemsLiveData.value = toDoItems
        }
    }

    fun getToDoItems(): LiveData<List<ToDoItem>> {
        return toDoItemsLiveData
    }

    fun getToDoItemsModifiable(): MutableLiveData<List<ToDoItem>> {
        return toDoItemsLiveData
    }

}