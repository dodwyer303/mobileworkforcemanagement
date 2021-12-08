package com.example.mobileworkforcemanagementapp.ui.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.example.mobileworkforcemanagementapp.ui.view.SignatureView
import com.example.mobileworkforcemanagementapp.viewmodel.TodoItemViewModel
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import java.io.File
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.view.ViewTreeObserver
import java.lang.Exception


class AddSignatureFragment: Fragment(R.layout.fragment_add_signature) {
    private var signatureView: SignatureView? = null
    private var closeTextView: TextView? = null
    private var addSignatureButton: Button? = null
    private var clearButton: Button? = null
    private var navController: NavController? = null
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private var todoItemViewModel: TodoItemViewModel? = null
    private var toDoItem: ToDoItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.get(view.context).getApplicationComponent().inject(this)
        navController = view.findNavController()
        addSignatureButton = view.findViewById(R.id.add_signature_button)
        clearButton = view.findViewById(R.id.clear_button)
        signatureView = view.findViewById(R.id.signature_view)
        closeTextView = view.findViewById(R.id.close_text_view)
        closeTextView?.setOnClickListener {
            navController?.popBackStack()
        }

        activity?.let {
            todoItemViewModel = ViewModelProvider(it as MainActivity, viewModelFactory)[TodoItemViewModel::class.java]
        }
        arguments.let {
            toDoItem = it?.get(ITEM_TO_ADD_SIGNATURE_BUNDLE_KEY) as ToDoItem? }
        addSignatureButton?.setOnClickListener {
            signatureView?.let {
                if (it.isStrokeAdded()) {
                    val bitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    it.draw(canvas)
                    if (todoItemViewModel != null &&  toDoItem != null) {
                        todoItemViewModel!!.addSignatureToToDoItem(bitmap, toDoItem!!)
                        navController?.popBackStack()
                    }
                } else {
                    Toast.makeText(view.context, "A signature has not yet been added!", Toast.LENGTH_LONG).show()
                }
            }
        }

        signatureView?.let {
            toDoItem?.signatureUrl?.let { signature ->
                if (signature.isNotEmpty()) {
                    it.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            try {
                                val file = File(signature)
                                val bmOptions = BitmapFactory.Options()
                                var bitmap = BitmapFactory.decodeFile(file.absolutePath, bmOptions)
                                bitmap =
                                    Bitmap.createScaledBitmap(bitmap!!, it.width, it.height, true)
                                it.setBitmap(bitmap)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }

                    })
                }
            }
        }
        clearButton?.setOnClickListener {
            signatureView?.clear()
        }
    }

    companion object {
        const val ITEM_TO_ADD_SIGNATURE_BUNDLE_KEY = "itemToAddSignature"
    }
}