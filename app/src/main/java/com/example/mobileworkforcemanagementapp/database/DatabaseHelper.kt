package com.example.mobileworkforcemanagementapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobileworkforcemanagementapp.model.ToDoItem

class DatabaseHelper(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "ToDoItemDatabase"
        private const val TABLE_TODO = "ToDoItemTable"
        private const val KEY_ID = "id"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_COMPLETED = "completed"
        private const val KEY_SIGNATURE_URL = "signatureUrl"
        private const val KEY_COMPLETED_DATE_TIME = "completedDateTime"
        private const val CREATE_TODO_TABLE = ("CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DESCRIPTION + " TEXT,"
                + KEY_SIGNATURE_URL + " TEXT DEFAULT '',"
                + KEY_COMPLETED_DATE_TIME + " INTEGER DEFAULT 0,"
                + KEY_COMPLETED + " INTEGER DEFAULT 0" + ")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        onCreate(db)
    }

    fun addToDoItem(toDoItem:ToDoItem): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DESCRIPTION, toDoItem.description)
        toDoItem.signatureUrl?. let{
            contentValues.put(KEY_SIGNATURE_URL, it)
        }
        toDoItem.completedDateTime?. let{
            contentValues.put(KEY_COMPLETED_DATE_TIME, it)
        }
        contentValues.put(KEY_COMPLETED, if(toDoItem.completed) 1 else 0 )
        val success = db.insert(TABLE_TODO, null, contentValues)
        db.close()
        return success
    }

    fun getTodoItems(): List<ToDoItem> {
        val toDoItemList: ArrayList<ToDoItem> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_TODO"
        val db = this.readableDatabase
        val cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        if (cursor.moveToFirst()) {
            do {
                val builder = ToDoItem.Builder()
                cursor?.getColumnIndex(KEY_ID)?.let {
                    builder.id(cursor.getInt(it))
                }

                cursor?.getColumnIndex(KEY_DESCRIPTION)?.let {
                    builder.description(cursor.getString(it))
                }

                cursor?.getColumnIndex(KEY_SIGNATURE_URL)?.let {
                    val signatureUrl = cursor.getString(it)
                    if (signatureUrl.isNotEmpty()) {
                        builder.signatureUrl(signatureUrl)
                    }
                }

                cursor?.getColumnIndex(KEY_COMPLETED_DATE_TIME)?.let {
                    val completedDateTime = cursor.getLong(it)
                    if (completedDateTime != 0L) {
                        builder.completedDateTime(completedDateTime)
                    }
                }

                cursor?.getColumnIndex(KEY_COMPLETED)?.let {
                    builder.completed(cursor.getInt(it) == 1)
                }
                toDoItemList.add(builder.build())
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return toDoItemList
    }

    fun updateTodoItem(toDoItem: ToDoItem): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, toDoItem.id)
        contentValues.put(KEY_DESCRIPTION, toDoItem.description)
        toDoItem.signatureUrl?. let{
            contentValues.put(KEY_SIGNATURE_URL, it)
        }
        toDoItem.completedDateTime?. let{
            contentValues.put(KEY_COMPLETED_DATE_TIME, it)
        }
        contentValues.put(KEY_COMPLETED, if(toDoItem.completed) 1 else 0 )
        val success = db.update(TABLE_TODO, contentValues, "$KEY_ID=${toDoItem.id}",null)
        db.close()
        return success
    }

    fun deleteToDoItem(toDoItem: ToDoItem): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, toDoItem.id)
        val success = db.delete(TABLE_TODO, "$KEY_ID=${toDoItem.id}",null)
        db.close()
        return success
    }

    fun deleteAllToDoItems() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_TODO")}
}