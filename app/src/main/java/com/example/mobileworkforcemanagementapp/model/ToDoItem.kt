package com.example.mobileworkforcemanagementapp.model
import java.io.Serializable

class ToDoItem(val id: Int?,val description: String?, val completed: Boolean): Serializable {

    private constructor(builder: Builder) : this(builder.id, builder.description, builder.completed)

    class Builder {
        var id: Int? = null
            private set

        var description: String? = null
            private set

        var completed: Boolean = false
            private set

        fun id(id: Int) = apply { this.id = id }

        fun description(description: String) = apply { this.description = description }

        fun completed(completed: Boolean) = apply { this.completed = completed }

        fun build() = ToDoItem(this)
    }
}