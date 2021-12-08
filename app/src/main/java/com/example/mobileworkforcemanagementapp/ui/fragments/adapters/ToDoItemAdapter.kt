package com.example.mobileworkforcemanagementapp.ui.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileworkforcemanagementapp.R
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ToDoItemAdapter constructor(private val todoListener: TodoListener): RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {
    private var itemList: List<ToDoItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_recycler_item, parent,
            false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.mTextDescription.text = holder.itemView.context.getString(R.string.completed_status_placeholder,
            holder.itemView.context.getString(R.string.description_title), item.description)
        holder.mButtonEdit.setOnClickListener { todoListener.onEditClicked(item) }
        holder.mButtonDelete.setOnClickListener { todoListener.onDeleteClicked(item) }
        if (item.completed) {
            holder.mButtonStatusChange.setOnClickListener { todoListener.onUndoClicked(item) }
            holder.mButtonStatusChange.text = holder.itemView.context.getString(R.string.undo_button_title)
        } else {
            holder.mButtonStatusChange.setOnClickListener { todoListener.onCompleteClicked(item) }
            holder.mButtonStatusChange.text = holder.itemView.context.getString(R.string.complete_button_title)
        }

        if (item.signatureUrl.isNullOrEmpty()) {
            holder.mSignatureImage.setImageResource(0)
            holder.mButtonAddSignature.text = holder.itemView.context.getString(R.string.add_signature_title)
            holder.mButtonAddSignature.setOnClickListener { todoListener.addSignatureClicked(item) }
        } else {
            val file = File(item.signatureUrl)
            Picasso.get().load(file).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.mSignatureImage)
            holder.mButtonAddSignature.text = holder.itemView.context.getString(R.string.remove_signature_title)
            holder.mButtonAddSignature.setOnClickListener { todoListener.removeSignatureClicked(item) }
            Picasso.get().invalidate(item.signatureUrl)
        }
        holder.mSignatureImage.setOnClickListener { todoListener.addSignatureClicked(item) }
        var statusString = holder.itemView.context.getString(if(item.completed) R.string.status_completed_title else R.string.status_not_complete_title)
        item.completedDateTime?.let {
            if (it > 0L && item.completed) {
                val simpleDateFormat = SimpleDateFormat(COMPLETED_DATE_TIME_FORMAT, Locale.getDefault())
                val completedDate = Date(it)
                statusString = "$statusString - ${simpleDateFormat.format(completedDate)}"
            }
        }
        holder.mTextStatus.text = statusString
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItems(todoItems: List<ToDoItem>) {
        itemList = todoItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTextDescription: TextView = mView.findViewById(R.id.todo_item_description_textview)
        val mTextStatus: TextView = mView.findViewById(R.id.todo_item_status_textview)
        val mButtonEdit: Button = mView.findViewById(R.id.todo_item_edit_button)
        val mButtonDelete: Button = mView.findViewById(R.id.todo_item_delete_button)
        val mButtonStatusChange: Button = mView.findViewById(R.id.todo_item_status_change_button)
        val mButtonAddSignature: Button = mView.findViewById(R.id.todo_item_add_signature_button)
        val mSignatureImage: ImageView = mView.findViewById(R.id.todo_item_signature_image_view)
    }

    companion object {
        const val COMPLETED_DATE_TIME_FORMAT = "EEE, MMM d, ''yy h:mm a"
    }
}