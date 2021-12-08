package com.example.mobileworkforcemanagementapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileworkforcemanagementapp.database.DatabaseHelper
import com.example.mobileworkforcemanagementapp.model.ToDoItem
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseHelperTest {
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var databaseHelper: DatabaseHelper

    @Before
    fun setup() {
        databaseHelper = DatabaseHelper(appContext)
        databaseHelper.deleteAllToDoItems()
    }
    @Test
    fun onCreate_shouldReturnNonEmptyList() {
        var items = databaseHelper.getTodoItems()
        assertEquals(0, items.size)
    }

    @Test
    fun whenItemsAdded_shouldReturnNonEmptyList() {
        databaseHelper.addToDoItem(ToDoItem.Builder().description("Item A").build())
        var items = databaseHelper.getTodoItems()
        assertEquals(1, items.size)
    }

    @Test
    fun whenItemAdded_shouldReflectOriginalState() {
        var originalItem = ToDoItem.Builder().description("Item A").build()
        databaseHelper.addToDoItem(originalItem)
        var items = databaseHelper.getTodoItems()
        var originalItemFromDatabase = items[0]
        assertEquals(true, originalItem.equalsIgnoreId(originalItemFromDatabase))
    }

    @Test
    fun whenItemUpdated_shouldReflectUpdatedState() {
        var originalItem = ToDoItem.Builder().description("Item A").build()
        databaseHelper.addToDoItem(originalItem)
        var items = databaseHelper.getTodoItems()
        var originalItemFromDatabase = items[0]
        assertEquals(true, originalItem.equalsIgnoreId(originalItemFromDatabase))

        var updatedItem = ToDoItem.Builder().id(originalItemFromDatabase.id!!)
            .description("Item A Edited").build()
        databaseHelper.updateTodoItem(updatedItem)
        var updateditems = databaseHelper.getTodoItems()
        var updatedItemFromDatabase = updateditems[0]
        assertEquals(true, updatedItem.equalsIgnoreId(updatedItemFromDatabase))
    }


    @Test
    fun whenItemDeleted_shouldReflectUpdatedItemCount() {
        var itemA = ToDoItem.Builder().description("Item A").build()
        var itemB = ToDoItem.Builder().description("Item B").build()
        databaseHelper.addToDoItem(itemA)
        databaseHelper.addToDoItem(itemB)
        var items = databaseHelper.getTodoItems()
        var itemAFromDatabase = items[0]
        var itemBFromDatabase = items[1]
        assertEquals(2, items.size)
        assertEquals(true, itemA.equalsIgnoreId(itemAFromDatabase))
        assertEquals(true, itemB.equalsIgnoreId(itemBFromDatabase))

        databaseHelper.deleteToDoItem(itemAFromDatabase)
        var updatedItems = databaseHelper.getTodoItems()
        var updatedItemFromDatabase = updatedItems[0]
        assertEquals(1, updatedItems.size)
        assertEquals(true, updatedItemFromDatabase.equalsIgnoreId(itemBFromDatabase))
    }
}