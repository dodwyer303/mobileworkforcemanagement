package com.example.mobileworkforcemanagementapp.ui.fragments.adapters

import com.example.mobileworkforcemanagementapp.model.ToDoItem

interface TodoListener {
    fun onEditClicked(toDoId: ToDoItem)

    fun onDeleteClicked(toDoId: ToDoItem)

    fun onUndoClicked(toDoId: ToDoItem)

    fun onCompleteClicked(toDoId: ToDoItem)
}