package com.example.mobileworkforcemanagementapp.model
import java.io.Serializable

class ToDoItem(val id: Int?, val description: String?, val completed: Boolean, val signatureUrl: String?): Serializable {

    private constructor(builder: Builder) : this(builder.id, builder.description, builder.completed, builder.signatureUrl)

    class Builder {
        var id: Int? = null
            private set

        var description: String? = null
            private set

        var completed: Boolean = false
            private set

        var signatureUrl: String? = null
            private set

        fun id(id: Int) = apply { this.id = id }

        fun description(description: String) = apply { this.description = description }

        fun completed(completed: Boolean) = apply { this.completed = completed }

        fun signatureUrl(signatureUrl: String) = apply { this.signatureUrl = signatureUrl }

        fun build() = ToDoItem(this)
    }
}