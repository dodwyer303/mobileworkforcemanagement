package com.example.mobileworkforcemanagementapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mobileworkforcemanagementapp.MyApplication
import com.example.mobileworkforcemanagementapp.R
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import com.example.mobileworkforcemanagementapp.ui.MainActivity
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import javax.inject.Inject

class AddEditFragment: Fragment(R.layout.fragment_add_edit) {
    private var addEditItemButton: Button? = null
    private var addEditFragmentTitle: TextView? = null
    private var closeTextView: TextView? = null
    private var descriptionEditText: EditText? = null
    private var navController: NavController? = null
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private var todoItemViewModel: TodoItemViewModel? = null
    private var editedtoDoItem: ToDoItem? = null
    private var fragmentState: FragmentState = FragmentState.ADD_ITEM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.get(view.context).getApplicationComponent().inject(this)
        editedtoDoItem = null
        addEditItemButton = view.findViewById(R.id.add_edit_item_button)
        addEditFragmentTitle = view.findViewById(R.id.add_edit_fragment_title)
        closeTextView = view.findViewById(R.id.close_text_view)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
        navController = view.findNavController()
        addEditItemButton?.setOnClickListener {
            when (fragmentState) {
                FragmentState.ADD_ITEM -> {
                    when {
                        !descriptionEditText?.editableText.isNullOrEmpty() -> {
                            todoItemViewModel?.addTodoItem(ToDoItem.Builder().description(descriptionEditText?.editableText.toString()).completed(false).build())
                            Toast.makeText(view.context, "New Item Added!",Toast.LENGTH_LONG).show()
                            navController?.popBackStack()
                        }
                        else -> {
                            Toast.makeText(view.context, "Description is empty!",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                FragmentState.EDIT_ITEM -> {
                    when {
                        !descriptionEditText?.editableText.isNullOrEmpty() -> {
                            editedtoDoItem?.let {
                                if(it.id != null) {
                                    todoItemViewModel?.updateToDoItem(ToDoItem.Builder().id(it.id).description(descriptionEditText?.editableText.toString()).completed(it.completed).build())
                                    Toast.makeText(view.context, "Item Updated!",Toast.LENGTH_LONG).show()
                                    navController?.popBackStack()
                                }
                            } ?: Toast.makeText(view.context, "Something went wrong!",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(view.context, "Description is empty!",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        closeTextView?.setOnClickListener {
            navController?.popBackStack()
        }
        activity?.let {
            todoItemViewModel = ViewModelProvider(it as MainActivity, viewModelFactory)[TodoItemViewModel::class.java]
        }
        arguments.let {
            editedtoDoItem = it?.get(ITEM_TO_EDIT_BUNDLE_KEY) as ToDoItem?
            fragmentState = if (editedtoDoItem == null) FragmentState.ADD_ITEM else FragmentState.EDIT_ITEM
        }
        val addEditString = if (fragmentState == FragmentState.ADD_ITEM) view.context.getString(R.string.add_item_title) else view.context.getString(R.string.update_item_title)
        addEditFragmentTitle?.text = addEditString
        addEditItemButton?.text = addEditString
        if(fragmentState == FragmentState.EDIT_ITEM && editedtoDoItem?.description != null) {
            descriptionEditText?.setText(editedtoDoItem?.description, TextView.BufferType.EDITABLE)
        }
    }

    enum class FragmentState {
        ADD_ITEM,
        EDIT_ITEM
    }

    companion object {
        const val ITEM_TO_EDIT_BUNDLE_KEY = "itemToEdit"
    }
}