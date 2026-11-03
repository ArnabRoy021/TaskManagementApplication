package com.example.taskmanagementapplication.state

import androidx.compose.runtime.mutableStateListOf
import com.example.taskmanagementapplication.data.SampleTasks
import com.example.taskmanagementapplication.model.Task

class TaskStore {

    private val _tasks = mutableStateListOf<Task>().apply {
        addAll(SampleTasks.tasks)
    }

    val tasks: List<Task>
        get() = _tasks

    fun findTask(id: Int): Task? {
        return _tasks.find { it.id == id }
    }

    fun addTask(task: Task) {
        _tasks.add(task)
    }

    fun updateTask(updatedTask: Task) {
        val index = _tasks.indexOfFirst {
            it.id == updatedTask.id
        }

        if (index != -1) {
            _tasks[index] = updatedTask
        }
    }

    fun deleteTask(id: Int) {
        _tasks.removeAll { it.id == id }
    }

    fun nextId(): Int {
        return (_tasks.maxOfOrNull { it.id } ?: 0) + 1
    }
}
