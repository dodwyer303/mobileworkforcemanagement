package com.example.mobileworkforcemanagementapp.ui.fragments.adapters

import com.example.mobileworkforcemanagementapp.model.ToDoItem

interface TodoListener {
    fun onEditClicked(toDoItem: ToDoItem)

    fun onDeleteClicked(toDoItem: ToDoItem)

    fun onUndoClicked(toDoItem: ToDoItem)

    fun onCompleteClicked(toDoItem: ToDoItem)

    fun addSignatureClicked(toDoItem: ToDoItem)

    fun removeSignatureClicked(toDoItem: ToDoItem)
}