package com.example.mobileworkforcemanagementapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileworkforcemanagementapp.MyApplication
import com.example.mobileworkforcemanagementapp.R
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import com.example.mobileworkforcemanagementapp.ui.MainActivity
import com.example.mobileworkforcemanagementapp.ui.fragments.adapters.ToDoItemAdapter
import com.example.mobileworkforcemanagementapp.ui.fragments.adapters.TodoListener
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import javax.inject.Inject

open class HomeFragment: Fragment(R.layout.fragment_home), TodoListener {
    private var todoRecyclerView: RecyclerView? = null
    private var addItemButton: Button? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var toDoItemAdapter: ToDoItemAdapter
    private var navController: NavController? = null
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private var todoItemViewModel: TodoItemViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.get(view.context).getApplicationComponent().inject(this)
        todoRecyclerView = view.findViewById(R.id.todo_recyclerview)
        addItemButton = view.findViewById(R.id.add_todo_button)
        linearLayoutManager = LinearLayoutManager(view.context)
        todoRecyclerView?.layoutManager = linearLayoutManager
        toDoItemAdapter = ToDoItemAdapter(this)
        todoRecyclerView?.adapter = toDoItemAdapter
        navController = view.findNavController()
        activity?.let {
            todoItemViewModel = ViewModelProvider(it as MainActivity, viewModelFactory)[TodoItemViewModel::class.java]
        }
        todoItemViewModel?.getToDoItems()?.observe(this, {
            it?.let {
               toDoItemAdapter.setListItems(it)
            }
        })
        addItemButton?.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment()
            navController?.navigate(action)
        }
    }

    override fun onEditClicked(toDoItem: ToDoItem) {
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(toDoItem)
            navController?.navigate(action)
    }

    override fun onDeleteClicked(toDoItem: ToDoItem){
        if(toDoItem.id != null) {
            todoItemViewModel?.deleteToDoItem(toDoItem)
        }
    }

    override fun onUndoClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null && toDoItem.completed) {
            var updatedToDoItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completed(false).build()
            todoItemViewModel?.updateToDoItem(updatedToDoItem)
        }
    }

    override fun onCompleteClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null && !toDoItem.completed) {
            var updatedToDoItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completed(true).build()
            todoItemViewModel?.updateToDoItem(updatedToDoItem)
        }
    }
}