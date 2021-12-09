package com.example.mobileworkforcemanagementapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.example.mobileworkforcemanagementapp.ui.fragments.adapters.SpacingItemDecoration
import com.example.mobileworkforcemanagementapp.ui.fragments.adapters.ToDoItemAdapter
import com.example.mobileworkforcemanagementapp.ui.fragments.adapters.TodoListener
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import java.util.*
import javax.inject.Inject

open class HomeFragment: Fragment(R.layout.fragment_home), TodoListener {
    private var todoRecyclerView: RecyclerView? = null
    private var addItemButton: Button? = null
    private var todoItemRadioGroup: RadioGroup? = null
    private var todoItemsEmptyTextView: TextView? = null
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
        todoItemRadioGroup = view.findViewById(R.id.todo_item_radio_group)
        todoItemsEmptyTextView = view.findViewById(R.id.todo_list_empty_title)
        linearLayoutManager = LinearLayoutManager(view.context)
        todoRecyclerView?.layoutManager = linearLayoutManager
        toDoItemAdapter = ToDoItemAdapter(this)
        todoRecyclerView?.adapter = toDoItemAdapter
        todoRecyclerView?.addItemDecoration(SpacingItemDecoration(16))
        navController = view.findNavController()
        todoItemRadioGroup?.setOnCheckedChangeListener { _, selectedId ->
            when (selectedId) {
                R.id.all_radio_button -> toDoItemAdapter.getFilterStateObservable().set(ToDoItemAdapter.FilterState.ALL)
                R.id.not_complete_radio_button -> toDoItemAdapter.getFilterStateObservable().set(ToDoItemAdapter.FilterState.NOT_COMPLETE)
                R.id.completed_radio_button -> toDoItemAdapter.getFilterStateObservable().set(ToDoItemAdapter.FilterState.COMPLETED)
            }
        }

        activity?.let {
            todoItemViewModel = ViewModelProvider(it as MainActivity, viewModelFactory)[TodoItemViewModel::class.java]
        }
        todoItemViewModel?.getToDoItems()?.observe(this, {
            it?.let {
                todoItemRadioGroup?.visibility = if(it.isEmpty()) View.INVISIBLE else View.VISIBLE
                todoRecyclerView?.visibility = if(it.isEmpty()) View.INVISIBLE else View.VISIBLE
                todoItemsEmptyTextView?.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
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
        AlertDialog.Builder(context!!, R.style.CustomAlertDialog).setTitle(getString(R.string.delete_message_title))
            .setNegativeButton(getString(R.string.no_choice_title), null)
            .setPositiveButton(getString(R.string.yes_choice_title)
        ) { _, _ ->
            if (toDoItem.id != null) {
                todoItemViewModel?.deleteToDoItem(toDoItem)
            }
        }.create().show()
    }

    override fun onUndoClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null && toDoItem.completed) {
            val updatedToDoItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completedDateTime(0L).completed(false).build()
            todoItemViewModel?.updateToDoItem(updatedToDoItem)
        }
    }

    override fun onCompleteClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null && !toDoItem.completed) {
            Calendar.getInstance().timeInMillis
            val updatedToDoItem = ToDoItem.Builder().id(toDoItem.id).description(toDoItem.description).completedDateTime(Calendar.getInstance().timeInMillis).completed(true).build()
            todoItemViewModel?.updateToDoItem(updatedToDoItem)
        }
    }

    override fun addSignatureClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null) {
            val action = HomeFragmentDirections.actionHomeFragmentToAddSignatureFragment(toDoItem)
            navController?.navigate(action)
        }
    }

    override fun removeSignatureClicked(toDoItem: ToDoItem) {
        if(toDoItem.id != null && toDoItem.description != null && !toDoItem.signatureUrl.isNullOrEmpty()) {
            todoItemViewModel?.removeSignatureFromToDoItem(toDoItem)
        }
    }
}