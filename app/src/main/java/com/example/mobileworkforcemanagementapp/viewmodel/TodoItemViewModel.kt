package com.example.mobileworkforcemanagementapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileworkforcemanagementapp.database.DatabaseHelper
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class TodoItemViewModel constructor(private val application: Application): ViewModel() {
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
        deleteSignatureFile(toDoItem)
        databaseHelper.deleteToDoItem(toDoItem)
        updateToDoItemsLiveDataFromDataBase()
    }

    private fun deleteSignatureFile(toDoItem: ToDoItem){
        toDoItem.signatureUrl?.let {
            if(it.isNotEmpty()) {
                val file: File?
                try {
                    file = File(it)
                    file.delete()
                }  catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun addSignatureToToDoItem(bitmap: Bitmap, toDoItem: ToDoItem) {
        val file: File?
        val pathname: String = application.getExternalFilesDir(null).toString() + File.separator + MOBILE_WORKFORCE_MANAGEMENT_APP_SIGNATURE + toDoItem.id
        var success: Boolean
        try {
            file = File(pathname)
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()
            success = file.let {
                val fos = FileOutputStream(it)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            success = false
        }

        if (success) {
            if(toDoItem.id != null && toDoItem.description != null) {
                val updatedItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completed(toDoItem.completed).signatureUrl(pathname).build()
                databaseHelper.updateTodoItem(updatedItem)
                updateToDoItemsLiveDataFromDataBase()
            }
        }
    }

    fun removeSignatureFromToDoItem(toDoItem: ToDoItem) {
        deleteSignatureFile(toDoItem)
        if(toDoItem.id != null && toDoItem.description != null) {
            val updatedItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completed(toDoItem.completed).signatureUrl("").build()
            databaseHelper.updateTodoItem(updatedItem)
            updateToDoItemsLiveDataFromDataBase()
        }
    }

    private fun updateToDoItemsLiveDataFromDataBase() {
        val toDoItems = databaseHelper.getTodoItems()
        toDoItemsLiveData.value = toDoItems
    }

    fun getToDoItems(): LiveData<List<ToDoItem>> {
        return toDoItemsLiveData
    }

    companion object {
        const val MOBILE_WORKFORCE_MANAGEMENT_APP_SIGNATURE = "mobileWorkforceManagementAppSignature"
    }

}