
package com.example.taskmanagementapplication.model

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val employeeName: String,
    val priority: TaskPriority,
    val dueDate: String,
    val status: TaskStatus
)
